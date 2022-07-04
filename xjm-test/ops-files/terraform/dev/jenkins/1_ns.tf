resource "kubernetes_namespace" "sre" {
  metadata {
    name = "foo"
  }
}

