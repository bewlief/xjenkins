
resource "kubernetes_deployment" "jenkins" {
  metadata {
    name      = "jenkins"
    namespace = "foo"
    labels = {
      app = "jenkins-sre"
      ns  = "foo"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "jenkins-sre"
        ns  = "foo"
      }
    }

    template {
      metadata {
        labels = {
          app = "jenkins-sre"
          ns  = "foo"
        }
      }

      spec {
        container {
          image = "docker-jenkins-sandbox:lts"
          name  = "jenkins-zh-lts"


          port {
            container_port = 8080
          #  protocol       = "TCP"
          #  host_port      = 28909
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
