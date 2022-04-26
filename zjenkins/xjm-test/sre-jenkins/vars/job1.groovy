def call(params) {
    node {
        stage("拉一个代码") {
            echo "in job1, stage 1"
        }
    }
}