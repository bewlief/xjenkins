def call(params) {
    node {
        stage("拉一个代码") {
            echo "in job2, stage 1"
        }
    }
}