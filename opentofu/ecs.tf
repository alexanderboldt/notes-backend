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
      name      = "notes-app"
      image     = "ghcr.io/alexanderboldt/notes:3.1.0"
      essential = true
      portMappings = [
        {
          containerPort = 4000
          hostPort      = 4000
        }
      ]
      environment = [
        { name = "MYSQL_URL",  value = "jdbc:mysql://${aws_db_instance.mysql.endpoint}/myappdb" },
        { name = "MYSQL_USER",  value = "admin" },
        { name = "MYSQL_PASSWORD",  value = "adminadmin" }
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
    from_port   = 4000
    to_port     = 4000
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
