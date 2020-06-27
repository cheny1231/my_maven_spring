package yangc.aop.advisor;

import java.util.List;

public interface AdvisorRegistry {
	void registerAdvisor(Advisor advisor);

	List<Advisor> getAdvisors();
}
