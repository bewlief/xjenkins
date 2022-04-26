
def failedBuild(opt = [:]) {
  def conf = [channel: "#jenkins-alerts", stageName: ""] << opt

  def trigger = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]
  def triggeredBy = trigger ? trigger['userName'] : '[webhook]'
  def attachments = [
    [
      'color': '#d62020',
      'pretext': 'Uh oh! A CI job has failed.',
      'author_name': 'Jenkins',
      'title': env.JOB_NAME,
      'title_link': env.RUN_DISPLAY_URL,
      'fields': [
        [
          'title': 'Status',
          'value': 'Failed',
          'short': true
        ],
        [
          'title': 'Triggered By',
          'value': triggeredBy,
          'short': true
        ],
        [
          'title': 'Stage',
          'value': conf.stageName,
          'short': true
        ]
      ]
    ]
  ]

  slackSend(channel: conf.channel, attachments: attachments)
}

def fixedBuild(opt = [:]) {
  def conf = [channel: "#jenkins-alerts"] << opt

  def trigger = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]
  def triggeredBy = trigger ? trigger['userName'] : '[webhook]'
  def attachments = [
    [
      'color': '#35d620',
      'pretext': 'A CI job has been fixed! :tada:',
      'author_name': 'Jenkins',
      'title': env.JOB_NAME,
      'title_link': env.RUN_DISPLAY_URL,
      'fields': [
        [
          'title': 'Status',
          'value': 'Fixed / Success',
          'short': true
        ],
        [
          'title': 'Triggered By',
          'value': triggeredBy,
          'short': true
        ],
        [
          'title': 'Stage',
          'value': conf.stageName,
          'short': true
        ]
      ]
    ]
  ]

  slackSend(channel: conf.channel, attachments: attachments)
}
