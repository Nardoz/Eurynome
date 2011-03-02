# Play! Twitter Connector

PlayFramework sample that connects to an Application on Twitter and allows to tweet on behalf of the user, similar to what you can do with TweetDeck

## Prerequisites

Before you can use Twitter authentication, you have to [register your application](http://developer.twitter.com/apps/new).
Make sure you set the application type to Browser and default access type to Read and Write.
Once you register your application, get the *Consumer key* and *Consumer secret*.

Copy conf/application.conf.template to conf/application.conf:

	cp -pR conf/application.conf.template conf/application.conf
	
Add to the bottom of the file keys you received from Twitter:

	# Twitter Credentials
	twitter.consumerKey=XXXXXXXXXXXXXXXXXXXXXXX
	twitter.consumerSecret=XXXXXXXXXXXXXXXXXXXXXXX
	
## Usage

Run your Play Application:

	play run .
	
Access the default URL: [http://localhost:9000](http://localhost:9000)

You will be able to:

- Sign-in: Be redirect to Twitter and approval your Twitter application to access your data
- Send DM: As any authorized User

## Notes

* These are my firsts steps on building app with Play! and Twitter, so don't expect any usable code for final/production app,  in fact it is more like a sandbox.
* You can check with User Id have been authorized through URL: [http://localhost:9000/admin/Users](http://localhost:9000/admin/Users)
* This app should be ported to a Play! Module, I'm still learning how to do this, that's why this is an APP instead of a Module
