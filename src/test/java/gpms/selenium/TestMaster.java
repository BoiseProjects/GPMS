package gpms.selenium;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		// SuccessfullProposalSubmition_IRB.class,
		// SuccessfullProposalSubmition_NoIRB.class,
		// SuccessfullProposalSubmitionIRB_twoFaculty.class,
		BusinessManagerApprovalFail.class,
		BusinessManagerAttatchmentFail.class, ChairApprovalFail.class,
		ChairAttatchmentFail.class, CoPIaddsSeniorPersonal.class,
		CoPIattemptsProposalDeletion.class, CoPIattemptsToDeleteCoPI.class,
		CoPIproposalSubmitionFail.class, DeanApprovalFail.class,
		DeanAttatchmentFail.class, DeanDeletesAttatchmentFail.class,
		DeanFileUploadFail.class, PIaddsCoPI.class,
		PIaddsFileAttatchment.class, PIdeletesAttatchment.class,
		PIdeletesCoPI.class, PIFileAttatchmentUpload.class,
		PIuploadsFileAttatchment.class, ProposalAddFailure.class,
		ResearchAdminApprovalFail.class, ResearchAdminProposalWithdrawn.class,
		ResearchDirectorApprovalFail.class,
		SeniorPersonalProposalDeleteFail.class,
		SeniorPersonalProposalSaveFail.class, TenuredChemFacultyProposal.class })
public class TestMaster {
	public TestMaster() {
	}
}
