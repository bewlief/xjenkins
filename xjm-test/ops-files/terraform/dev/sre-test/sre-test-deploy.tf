
resource "kubernetes_deployment" "sre-test" {
  metadata {
    name      = "sre-test"
    namespace = "foo"
    labels = {
      app = "sre-test"
      ns  = "foo"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "sre-test"
        ns  = "foo"
      }
    }

    template {
      metadata {
        labels = {
          app = "sre-test"
          ns  = "foo"
        }
      }

      spec {
        container {
          image = "sre-test:v1"
          name  = "sre-test-v1"


          port {
            container_port = 8800
          #  protocol       = "TCP"
            host_port      = 28909
          }

          resources {
            limits {
              cpu    = "0.5"
              memory = "512Mi"
            }
            requests {
              cpu    = "250m"
              memory = "50Mi"
            }
          }


        }
      }
    }
  }
}
