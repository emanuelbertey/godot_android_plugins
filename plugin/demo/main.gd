extends Node2D

# TODO: Update to match your plugin's name
var _plugin_name = "GodotAndroidPlugin"
var _android_plugin
var plugin
#func _ready():
	#if Engine.has_singleton(_plugin_name):
		#_android_plugin = Engine.get_singleton(_plugin_name)
	#else:
		#printerr("Couldn't find plugin " + _plugin_name)

func _on_Button_pressed():
	if _android_plugin:
		# TODO: Update to match your plugin's API
		_android_plugin.helloWorld()





func _ready():
	if Engine.has_singleton("GodotAndroidPlugin"):
		plugin = Engine.get_singleton("GodotAndroidPlugin")
		plugin.helloWorld()
		plugin.start_foreground_service()
	else:
		printerr("Plugin no disponible")
