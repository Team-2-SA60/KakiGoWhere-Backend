terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "2.60.0"
    }
  }
}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_droplet" "app" {
  name       = "springboot-local-test"
  region     = "sgp1"
  image      = "ubuntu-22-04-x64"
  size       = "s-1vcpu-1gb"
  ssh_keys   = [var.ssh_key_fingerprint]
}

output "droplet_ip" {
  value = digitalocean_droplet.app.ipv4_address
}