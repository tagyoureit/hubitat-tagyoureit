# Generic Telnet Driver

This is a generic telnet driver to allow you to send commands to any device.  Currently, there is no username/password option.

I use this driver to extend the capabilities of custom/other drivers.  EG, I have a Marantz Receiver and I'd like to be able to have fine grain control over the picture and sound modes.  The system driver doesn't allow this.

## Instructions
1. Install the driver
2. Create a virtual device and assign it the same IP address as the real device
3. Use this in Rule Manager or SharpTools Flow to send a custom command

The `Device Command` and `Device Command and Parameter` are identical and provided just for convenience.  The difference is that the command + parameter will concatenate the two string values.  
If you have a device that takes a command with spaces, and want to use the command + parameter, add a space at the end of the command.
