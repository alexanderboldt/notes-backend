locals {
  database = {
    name = "notes"
    username = "admin"
    password = "adminadmin"
    port = 3306
  }

  notes_app = {
    name = "notes-app"
    image = "ghcr.io/alexanderboldt/notes:3.1.0"
    port = 4000
  }
}
