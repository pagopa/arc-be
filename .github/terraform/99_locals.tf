locals {
  # Repo
  github = {
    org        = "pagopa"
    repository = "arc-be"
  }

  repo_secrets = var.env_short == "p" ? {
    SONAR_TOKEN = data.azurerm_key_vault_secret.sonar_token[0].value
  } : {}

  map_repo = {
    "dev" : "*",
    "uat" : "uat"
    "prod" : "main"
  }
}
