terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "2.60.0"
    }
  }
}

terraform {
  cloud {

    organization = "Team2_SA60"

    workspaces {
      name = "KakiGoWhere"
    }
  }
}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_droplet" "app" {
  name       = "KakiGoWhere"
  region     = "sgp1"
  image      = "ubuntu-24-04-x64"
  size       = "s-4vcpu-8gb-240gb-intel"
  ssh_keys   = [var.ssh_key_fingerprint]
}

output "droplet_ip" {
  value = digitalocean_droplet.app.ipv4_address
}