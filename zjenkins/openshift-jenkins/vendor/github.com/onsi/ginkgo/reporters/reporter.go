package reporters

import (
	"github.com/onsi/ginkgo/securitydemo.config"
	"github.com/onsi/ginkgo/types"
)

type Reporter interface {
	SpecSuiteWillBegin(securitydemo.config securitydemo.config.GinkgoConfigType, summary *types.SuiteSummary)
	BeforeSuiteDidRun(setupSummary *types.SetupSummary)
	SpecWillRun(specSummary *types.SpecSummary)
	SpecDidComplete(specSummary *types.SpecSummary)
	AfterSuiteDidRun(setupSummary *types.SetupSummary)
	SpecSuiteDidEnd(summary *types.SuiteSummary)
}
