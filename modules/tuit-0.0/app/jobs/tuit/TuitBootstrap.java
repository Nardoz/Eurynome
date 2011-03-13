package jobs.tuit;

import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import services.tuit.TuitService;

@OnApplicationStart
public class TuitBootstrap extends Job {
	
	public void doJob() {
		TuitService.checkConfigutation();
	}
	
}
