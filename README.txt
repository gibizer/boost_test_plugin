To make the plugin runnable from eclipse add the following to the Window/Preferences/PluginDevelopment/TargetPlatform/Edit/Arguments/VMarguments
-Xms40m -Xmx512m -XX:PermSize=256M -XX:MaxPermSize=512M
Then delete every launch configuration related to this plugin

