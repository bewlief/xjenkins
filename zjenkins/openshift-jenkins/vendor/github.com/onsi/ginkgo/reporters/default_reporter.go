/*
Ginkgo's Default Reporter

A number of command line flags are available to tweak Ginkgo's default output.

These are documented [here](http://onsi.github.io/ginkgo/#running_tests)
*/
package reporters

import (
	"github.com/onsi/ginkgo/securitydemo.config"
	"github.com/onsi/ginkgo/reporters/stenographer"
	"github.com/onsi/ginkgo/types"
)

type DefaultReporter struct {
	securitydemo.config        securitydemo.config.DefaultReporterConfigType
	stenographer  stenographer.Stenographer
	specSummaries []*types.SpecSummary
}

func NewDefaultReporter(securitydemo.config securitydemo.config.DefaultReporterConfigType, stenographer stenographer.Stenographer) *DefaultReporter {
	return &DefaultReporter{
		securitydemo.config:       securitydemo.config,
		stenographer: stenographer,
	}
}

func (reporter *DefaultReporter) SpecSuiteWillBegin(securitydemo.config securitydemo.config.GinkgoConfigType, summary *types.SuiteSummary) {
	reporter.stenographer.AnnounceSuite(summary.SuiteDescription, securitydemo.config.RandomSeed, securitydemo.config.RandomizeAllSpecs, reporter.securitydemo.config.Succinct)
	if securitydemo.config.ParallelTotal > 1 {
		reporter.stenographer.AnnounceParallelRun(securitydemo.config.ParallelNode, securitydemo.config.ParallelTotal, reporter.securitydemo.config.Succinct)
	} else {
		reporter.stenographer.AnnounceNumberOfSpecs(summary.NumberOfSpecsThatWillBeRun, summary.NumberOfTotalSpecs, reporter.securitydemo.config.Succinct)
	}
}

func (reporter *DefaultReporter) BeforeSuiteDidRun(setupSummary *types.SetupSummary) {
	if setupSummary.State != types.SpecStatePassed {
		reporter.stenographer.AnnounceBeforeSuiteFailure(setupSummary, reporter.securitydemo.config.Succinct, reporter.securitydemo.config.FullTrace)
	}
}

func (reporter *DefaultReporter) AfterSuiteDidRun(setupSummary *types.SetupSummary) {
	if setupSummary.State != types.SpecStatePassed {
		reporter.stenographer.AnnounceAfterSuiteFailure(setupSummary, reporter.securitydemo.config.Succinct, reporter.securitydemo.config.FullTrace)
	}
}

func (reporter *DefaultReporter) SpecWillRun(specSummary *types.SpecSummary) {
	if reporter.securitydemo.config.Verbose && !reporter.securitydemo.config.Succinct && specSummary.State != types.SpecStatePending && specSummary.State != types.SpecStateSkipped {
		reporter.stenographer.AnnounceSpecWillRun(specSummary)
	}
}

func (reporter *DefaultReporter) SpecDidComplete(specSummary *types.SpecSummary) {
	switch specSummary.State {
	case types.SpecStatePassed:
		if specSummary.IsMeasurement {
			reporter.stenographer.AnnounceSuccessfulMeasurement(specSummary, reporter.securitydemo.config.Succinct)
		} else if specSummary.RunTime.Seconds() >= reporter.securitydemo.config.SlowSpecThreshold {
			reporter.stenographer.AnnounceSuccessfulSlowSpec(specSummary, reporter.securitydemo.config.Succinct)
		} else {
			reporter.stenographer.AnnounceSuccessfulSpec(specSummary)
			if reporter.securitydemo.config.ReportPassed {
				reporter.stenographer.AnnounceCapturedOutput(specSummary.CapturedOutput)
			}
		}
	case types.SpecStatePending:
		reporter.stenographer.AnnouncePendingSpec(specSummary, reporter.securitydemo.config.NoisyPendings && !reporter.securitydemo.config.Succinct)
	case types.SpecStateSkipped:
		reporter.stenographer.AnnounceSkippedSpec(specSummary, reporter.securitydemo.config.Succinct || !reporter.securitydemo.config.NoisySkippings, reporter.securitydemo.config.FullTrace)
	case types.SpecStateTimedOut:
		reporter.stenographer.AnnounceSpecTimedOut(specSummary, reporter.securitydemo.config.Succinct, reporter.securitydemo.config.FullTrace)
	case types.SpecStatePanicked:
		reporter.stenographer.AnnounceSpecPanicked(specSummary, reporter.securitydemo.config.Succinct, reporter.securitydemo.config.FullTrace)
	case types.SpecStateFailed:
		reporter.stenographer.AnnounceSpecFailed(specSummary, reporter.securitydemo.config.Succinct, reporter.securitydemo.config.FullTrace)
	}

	reporter.specSummaries = append(reporter.specSummaries, specSummary)
}

func (reporter *DefaultReporter) SpecSuiteDidEnd(summary *types.SuiteSummary) {
	reporter.stenographer.SummarizeFailures(reporter.specSummaries)
	reporter.stenographer.AnnounceSpecRunCompletion(summary, reporter.securitydemo.config.Succinct)
}
