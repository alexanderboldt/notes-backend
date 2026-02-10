resource "aws_s3_bucket" "notes_bucket" {
  bucket = "notes-prod-files-euc1-9d2a"

  tags = {
    Environment = "prod"
    Project     = "notes-app"
  }
}

resource "aws_iam_policy" "ecs_s3_policy" {
  name        = "notes-ecs-s3-policy"
  description = "Allow ECS task to access S3 bucket"
  policy      = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = [
          "s3:PutObject",
          "s3:GetObject",
          "s3:DeleteObject"
        ]
        Resource = "${aws_s3_bucket.notes_bucket.arn}/*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_policy_attach" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.ecs_s3_policy.arn
}
