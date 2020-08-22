/**
 *  Generic Telnet Driver
 *  
 *  Copyright 2020 Russ Goldin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *    
 *
 *  Change Log:
 *  2020-08-22: Initial
 *
 */

metadata {
  definition(name: "Generic Telnet Driver", namespace: "tagyoureit-hubitat", author: "Russ Goldin") {
    capability "Telnet"
    capability "Initialize"
    capability "Refresh"

    command "deviceCommandAndParameter", [[name:"Command", type: "STRING", description: "Command", constraints: ["STRING"]], [name:"Payload", type: "STRING", description: "Payload", constraints: ["STRING"]]]
    command "deviceCommand", [[name:"Command", type: "STRING", description: "Command and Parameter as a single string", constraints: ["STRING"]]]
  }

  preferences {
    section("Device Settings:") {
      input "IP", "string", title: "IP Address", description: "IP Address", required: true, displayDuringSetup: true
      input name: "descriptionTextEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: false
      input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: false
      input name: "traceLogEnable", type: "bool", title: "Enable trace logging", defaultValue: false
    }
  }
}

def installed() {
  logInfo('installed()')
  initialize()
}

def updated() {
  logInfo('updated()')
  updateDNI()
  initialize()
}

def initialize() {
  logInfo('initialize()')
  telnetClose()
  telnetConnect([termChars:[13]], IP, 23, null/*username*/, null/*password*/)
}


def deviceCommandAndParameter(command, payload) {
    sendMsg("${command}${payload}")
}

def deviceCommand(command) {
    sendMsg("${command}")
}

def sendMsg(String msg) {
  logDebug("Sending telnet msg: " + msg)
  return new hubitat.device.HubAction(msg, hubitat.device.Protocol.TELNET)
}

def parse(String msg) {
  logDebug("Parse: " + msg + " len: " + msg.length())
}

private updateDNI() {
  def dni = getHexHostAddress()
  if (dni != device.deviceNetworkId) {
    device.deviceNetworkId = dni
  }
}

private String convertIPtoHex(ipAddress) {
  return ipAddress.tokenize('.').collect { String.format('%02x', it.toInteger()) }.join().toUpperCase()
}

private String convertPortToHex(port) {
  String hexport = port.toString().format('%04x', 23)
  logDebug "The converted hex port is ${hexport}"
  return hexport.toUpperCase()
}

private getHexHostAddress() {
  def hosthex = convertIPtoHex(settings.IP)
  def porthex = convertPortToHex(23)
  if (porthex.length() < 4) {
    porthex = "00" + porthex
  }
  logDebug "Hosthex is : $hosthex"
  logDebug "Port in Hex is $porthex"
  return "${hosthex}:${porthex}"
  return "${hosthex}"
}

private logInfo(msg) {
  if (descriptionTextEnable) log.info msg
}

def logDebug(msg) {
  if (logEnable) log.debug msg
}

def logTrace(msg) {
  if (traceLogEnable) log.trace msg
}
