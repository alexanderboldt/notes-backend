locals {
  database = {
    name = "notes"
    username = "admin"
    password = "adminadmin"
    port = 3306
  }

  s3 = {
    region = "eu-central-1"
  }

  notes_app = {
    name = "notes-app"
    image = "ghcr.io/alexanderboldt/notes:5.1.0"
    port = 4000
  }
}
