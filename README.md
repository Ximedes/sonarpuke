# sonarpuke
Replacement of the missing SonarQube projects overview page.

We're using the [web API](http://sonar.chess.int/web_api) to
recreate the original project overview page which was 
[removed from SonarQube](https://jira.sonarsource.com/browse/SONAR-7915).

## Building your dashboard
To create an index.html file which represents your trusty old Sonar
dashboard page, run the following commands:

```
$ git clone https://github.com/Ximedes/sonarpuke.git
$ cd sonarpuke
$ ./sonarpuke.sh \
  http://your.sonar.install \
  target/classes/dashboard.vm \
  target/index.html
$ open index.html
```

The index.html file will contain an overview of all projects in your
Sonarcube installation, one line per project, with the quality gate
information you were used to in the old project overview page.

The links on the page will take you to the project homepage.
 
We hope this helps in restoring your original wallboard.
 
(Note: This is a rough draft, the metrics are missing and the css
is not done. If you have tips let us know.)
