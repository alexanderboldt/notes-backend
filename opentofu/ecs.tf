resource "aws_ecs_cluster" "app_cluster" {
  name = "notes-app-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "notes-app-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([
    {
      name      = local.notes_app.name
      image     = local.notes_app.image
      essential = true
      portMappings = [
        {
          containerPort = local.notes_app.port
          hostPort      = local.notes_app.port
        }
      ]
      environment = [
        { name = "MYSQL_URL",  value = "jdbc:mysql://${aws_db_instance.mysql.endpoint}/${local.database.name}" },
        { name = "MYSQL_USER",  value = local.database.username },
        { name = "MYSQL_PASSWORD",  value = local.database.password }
      ]
    }
  ])
}

resource "aws_ecs_service" "app_service" {
  name            = "notes-app-service"
  cluster         = aws_ecs_cluster.app_cluster.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.public_a.id, aws_subnet.public_b.id]
    security_groups = [aws_security_group.ecs_sg.id]
    assign_public_ip = true   # <-- important for GHCR image pull
  }
}

resource "aws_security_group" "ecs_sg" {
  name        = "ecs-sg"
  description = "ECS tasks security group"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = local.notes_app.port
    to_port     = local.notes_app.port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
