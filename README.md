# Play! Eurynome

_Bringing order to your twitter chaos..._

**Eurynome lets you easily extract, process and visualize Twitter info**

Eurynome is an open-source project maintained by the [[NardosRulez|http://groups.google.com/group/nardosrulez] team.

It was developed as an experiment to test most of [[Play Framework's|http://www.playframework.org/] capabilities and other cutting-edge technologies, which include:

* Background Jobs
* Neo4J Graph Database
* oAuth Client
* Play Framework 1.2
* Webservices (REST Client)
* Websockets

**Disclaimer: Eurynome is a work in progress, so be prepared to find broken things, and be willing to get your hands dirty to fix them!**

PlayFramework sample that connects to an Application on Twitter and allows to tweet on behalf of the user, similar to what you can do with TweetDeck

## Prerequisites

Before you can use Eurynome, you have to [register your application](http://developer.twitter.com/apps/new).
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

