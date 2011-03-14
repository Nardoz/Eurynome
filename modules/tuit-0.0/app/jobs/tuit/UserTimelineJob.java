package jobs.tuit;

import java.io.InputStream;
import java.util.concurrent.Future;

import models.tuit.TuitAccount;
import models.tuit.TuitUser;
import play.Logger;
import play.jobs.Job;
import services.tuit.TuitService;
import services.tuit.UserService;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class UserTimelineJob extends Job {
	
	private Long id;
	
	public UserTimelineJob(Long id) {
		this.id = id;
	}

	public Future<InputStream> doJobWithResult() {		
		TuitAccount account = TuitAccount.findById(id);
		
		if(account == null) {
			Logger.error("access.id not found: " + id);
			return null;
		}
		
		if(account.timelineProcessed) {
			Logger.error("already processed");
			return null;
		}
		
		Twitter twitter = TuitService.factory(account);

		try {
			TuitUser me = UserService.find(account.userId);
			if (null == me) {
				User user = twitter.showUser(account.screenName);
				me = UserService.findOrCreate(user);
			}

			Paging paging = new Paging(1, 100);
			ResponseList<Status> mentions = twitter.getMentions(paging);
			ResponseList<Status> timeline = null;

			for (int i = 0; i < 15; i++) {
				paging = new Paging(i + 1, 100);
				timeline = twitter.getHomeTimeline(paging);

				if (timeline.size() == 0)
					break;
				
				// I'm following these users
				for (Status status : timeline) {
					User follower = status.getUser();
					TuitUser follow2 = UserService.setFollowing(me, follower);
					
					// Reads followers information on each client.
					// TODO: Save list of UserId and lookup later

					// IDs followersIDs =
					// twitter.getFollowersIDs(follower.getId());
					// ResponseList<User> ff =
					// twitter.lookupUsers(followersIDs.getIDs());
					// for (User f : ff) {
					// UserService.setFollowing(follow2, f);
					// }

				}
			}

			// I can't establish relationship yet.
			// just save user information
			//
			for (Status status : mentions) {
				User user = status.getUser();
				UserService.findOrCreate(user);
			}
			
			account.timelineProcessed = true;
			account.save();

		} catch (TwitterException e) {
			Logger.error(e, e.toString(), "");
			//error(e.getMessage());
		}
		return null;
	}
}
