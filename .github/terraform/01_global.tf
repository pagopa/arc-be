resource "github_branch" "release" {
  for_each      = var.env == "prod" ? toset(local.branches) : []
  repository    = local.github.repository
  branch        = each.key
  source_branch = "main"
}

resource "github_branch_default" "default" {
  repository = local.github.repository
  branch     = "develop"
}

resource "github_repository_ruleset" "branch_rules" {
  for_each = var.env == "prod" ? local.branch_rulesets : {}

  name        = each.key
  repository  = local.github.repository
  target      = "branch"
  enforcement = "active"

  conditions {
    ref_name {
      include = [each.value.ref_name]
      exclude = []
    }
  }

  dynamic "bypass_actors" {
    for_each = each.value.bypass_actors == true ? toset(local.bypass_branch_rules_teams) : []
    content {
      actor_id    = lookup(local.team_name_to_id, bypass_actors.value)
      actor_type  = "Team"
      bypass_mode = "always"
    }
  }

  rules {
    creation                = false
    update                  = false
    deletion                = true
    required_signatures     = false
    required_linear_history = each.value.required_linear_history

    pull_request {
      dismiss_stale_reviews_on_push     = false
      require_last_push_approval        = false
      required_review_thread_resolution = false
      require_code_owner_review         = each.value.require_code_owner_review
      required_approving_review_count   = each.value.required_approving_review_count
    }
  }
}
