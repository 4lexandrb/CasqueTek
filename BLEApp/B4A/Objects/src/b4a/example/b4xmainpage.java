package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xmainpage extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "b4a.example.b4xmainpage");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", b4a.example.b4xmainpage.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _root = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _btnreaddata = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _btndisconnect = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _btnscan = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lbldevicestatus = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _lblstate = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _label3 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _label4 = null;
public b4a.example.b4xloadingindicator _pbreaddata = null;
public b4a.example3.customlistview _clv = null;
public anywheresoftware.b4a.objects.BleManager2 _manager = null;
public anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public String _currentstatetext = "";
public int _currentstate = 0;
public boolean _connected = false;
public String _connectedname = "";
public anywheresoftware.b4a.objects.collections.List _connectedservices = null;
public b4a.example.b4xloadingindicator _pbscan = null;
public float _dist = 0f;
public float _choc = 0f;
public boolean _bstart = false;
public anywheresoftware.b4a.phone.Phone.PhoneVibrate _vibrate = null;
public anywheresoftware.b4a.audio.Beeper _b = null;
public b4a.example.dateutils _dateutils = null;
public b4a.example.main _main = null;
public b4a.example.starter _starter = null;
public b4a.example.b4xpages _b4xpages = null;
public b4a.example.b4xcollections _b4xcollections = null;
public b4a.example.xuiviewsutils _xuiviewsutils = null;
public String  _b4xpage_created(anywheresoftware.b4a.objects.B4XViewWrapper _root1) throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Private Sub B4XPage_Created (Root1 As B4XView)";
 //BA.debugLineNum = 49;BA.debugLine="Root = Root1";
_root = _root1;
 //BA.debugLineNum = 50;BA.debugLine="Root.LoadLayout(\"1\")";
_root.LoadLayout("1",ba);
 //BA.debugLineNum = 51;BA.debugLine="B4XPages.SetTitle(Me, \"BLE Example\")";
_b4xpages._settitle /*String*/ (ba,this,(Object)("BLE Example"));
 //BA.debugLineNum = 52;BA.debugLine="manager.Initialize(\"manager\")";
_manager.Initialize(ba,"manager");
 //BA.debugLineNum = 53;BA.debugLine="StateChanged";
_statechanged();
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public String  _btndisconnect_click() throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub btnDisconnect_Click";
 //BA.debugLineNum = 95;BA.debugLine="manager.Disconnect";
_manager.Disconnect();
 //BA.debugLineNum = 96;BA.debugLine="Manager_Disconnected";
_manager_disconnected();
 //BA.debugLineNum = 97;BA.debugLine="bStart = False";
_bstart = __c.False;
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public String  _btnreaddata_click() throws Exception{
String _s = "";
 //BA.debugLineNum = 100;BA.debugLine="Sub btnReadData_Click";
 //BA.debugLineNum = 101;BA.debugLine="pbReadData.Show";
_pbreaddata._show /*String*/ ();
 //BA.debugLineNum = 103;BA.debugLine="For Each s As String In ConnectedServices";
{
final anywheresoftware.b4a.BA.IterableList group2 = _connectedservices;
final int groupLen2 = group2.getSize()
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_s = BA.ObjectToString(group2.Get(index2));
 //BA.debugLineNum = 104;BA.debugLine="manager.ReadData(s)";
_manager.ReadData(_s);
 }
};
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public void  _btnscan_click() throws Exception{
ResumableSub_btnScan_Click rsub = new ResumableSub_btnScan_Click(this);
rsub.resume(ba, null);
}
public static class ResumableSub_btnScan_Click extends BA.ResumableSub {
public ResumableSub_btnScan_Click(b4a.example.b4xmainpage parent) {
this.parent = parent;
}
b4a.example.b4xmainpage parent;
anywheresoftware.b4a.objects.collections.List _permissions = null;
anywheresoftware.b4a.phone.Phone _phone = null;
String _per = "";
String _permission = "";
boolean _result = false;
anywheresoftware.b4a.BA.IterableList group8;
int index8;
int groupLen8;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 74;BA.debugLine="Dim Permissions As List";
_permissions = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 75;BA.debugLine="Dim phone As Phone";
_phone = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 76;BA.debugLine="If phone.SdkVersion >= 31 Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_phone.getSdkVersion()>=31) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 77;BA.debugLine="Permissions = Array(\"android.permission.BLUETOOT";
_permissions = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)("android.permission.BLUETOOTH_SCAN"),(Object)("android.permission.BLUETOOTH_CONNECT"),(Object)(parent._rp.PERMISSION_ACCESS_FINE_LOCATION)});
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 79;BA.debugLine="Permissions = Array(rp.PERMISSION_ACCESS_FINE_LO";
_permissions = anywheresoftware.b4a.keywords.Common.ArrayToList(new Object[]{(Object)(parent._rp.PERMISSION_ACCESS_FINE_LOCATION)});
 if (true) break;
;
 //BA.debugLineNum = 81;BA.debugLine="For Each per As String In Permissions";

case 6:
//for
this.state = 13;
group8 = _permissions;
index8 = 0;
groupLen8 = group8.getSize();
this.state = 14;
if (true) break;

case 14:
//C
this.state = 13;
if (index8 < groupLen8) {
this.state = 8;
_per = BA.ObjectToString(group8.Get(index8));}
if (true) break;

case 15:
//C
this.state = 14;
index8++;
if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 82;BA.debugLine="rp.CheckAndRequest(per)";
parent._rp.CheckAndRequest(ba,_per);
 //BA.debugLineNum = 83;BA.debugLine="Wait For B4XPage_PermissionResult (Permission As";
parent.__c.WaitFor("b4xpage_permissionresult", ba, this, null);
this.state = 16;
return;
case 16:
//C
this.state = 9;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 84;BA.debugLine="If Result = False Then";
if (true) break;

case 9:
//if
this.state = 12;
if (_result==parent.__c.False) { 
this.state = 11;
}if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 85;BA.debugLine="ToastMessageShow(\"No permission: \" & Permission";
parent.__c.ToastMessageShow(BA.ObjectToCharSequence("No permission: "+_permission),parent.__c.True);
 //BA.debugLineNum = 86;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 12:
//C
this.state = 15;
;
 if (true) break;
if (true) break;

case 13:
//C
this.state = -1;
;
 //BA.debugLineNum = 90;BA.debugLine="pbScan.Show";
parent._pbscan._show /*String*/ ();
 //BA.debugLineNum = 91;BA.debugLine="StartScan";
parent._startscan();
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public void  _b4xpage_permissionresult(String _permission,boolean _result) throws Exception{
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private Root As B4XView";
_root = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 11;BA.debugLine="Private btnReadData As B4XView";
_btnreaddata = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 12;BA.debugLine="Private btnDisconnect As B4XView";
_btndisconnect = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private btnScan As B4XView";
_btnscan = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Private lblDeviceStatus As B4XView";
_lbldevicestatus = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private lblState As B4XView";
_lblstate = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Private Label3 As B4XView";
_label3 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private Label4 As B4XView";
_label4 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private pbReadData As B4XLoadingIndicator";
_pbreaddata = new b4a.example.b4xloadingindicator();
 //BA.debugLineNum = 19;BA.debugLine="Private clv As CustomListView";
_clv = new b4a.example3.customlistview();
 //BA.debugLineNum = 21;BA.debugLine="Private manager As BleManager2";
_manager = new anywheresoftware.b4a.objects.BleManager2();
 //BA.debugLineNum = 22;BA.debugLine="Private rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 26;BA.debugLine="Private currentStateText As String = \"UNKNOWN\"";
_currentstatetext = "UNKNOWN";
 //BA.debugLineNum = 27;BA.debugLine="Private currentState As Int";
_currentstate = 0;
 //BA.debugLineNum = 28;BA.debugLine="Private connected As Boolean = False";
_connected = __c.False;
 //BA.debugLineNum = 29;BA.debugLine="Private ConnectedName As String";
_connectedname = "";
 //BA.debugLineNum = 30;BA.debugLine="Private ConnectedServices As List";
_connectedservices = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 31;BA.debugLine="Private pbScan As B4XLoadingIndicator";
_pbscan = new b4a.example.b4xloadingindicator();
 //BA.debugLineNum = 32;BA.debugLine="Private dist As Float = 0";
_dist = (float) (0);
 //BA.debugLineNum = 33;BA.debugLine="Private choc As Float = 0";
_choc = (float) (0);
 //BA.debugLineNum = 34;BA.debugLine="Private bStart As Boolean = False";
_bstart = __c.False;
 //BA.debugLineNum = 35;BA.debugLine="Dim Vibrate As PhoneVibrate";
_vibrate = new anywheresoftware.b4a.phone.Phone.PhoneVibrate();
 //BA.debugLineNum = 36;BA.debugLine="Dim b As Beeper";
_b = new anywheresoftware.b4a.audio.Beeper();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 41;BA.debugLine="Public Sub Initialize";
 //BA.debugLineNum = 43;BA.debugLine="b.Initialize(300, 500)";
_b.Initialize((int) (300),(int) (500));
 //BA.debugLineNum = 44;BA.debugLine="b.Beep";
_b.Beep();
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public String  _manager_connected(anywheresoftware.b4a.objects.collections.List _services) throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub Manager_Connected (services As List)";
 //BA.debugLineNum = 227;BA.debugLine="Log(\"Connected\")";
__c.LogImpl("81507329","Connected",0);
 //BA.debugLineNum = 228;BA.debugLine="connected = True";
_connected = __c.True;
 //BA.debugLineNum = 229;BA.debugLine="ConnectedServices = services";
_connectedservices = _services;
 //BA.debugLineNum = 230;BA.debugLine="StateChanged";
_statechanged();
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public String  _manager_dataavailable(String _serviceid,anywheresoftware.b4a.objects.collections.Map _characteristics) throws Exception{
anywheresoftware.b4a.agraham.byteconverter.ByteConverter _bc = null;
String _id = "";
byte[] _data1 = null;
 //BA.debugLineNum = 175;BA.debugLine="Sub Manager_DataAvailable (ServiceId As String, Ch";
 //BA.debugLineNum = 176;BA.debugLine="If connected Then";
if (_connected) { 
 //BA.debugLineNum = 177;BA.debugLine="pbReadData.Hide";
_pbreaddata._hide /*String*/ ();
 //BA.debugLineNum = 178;BA.debugLine="Log(ServiceId)";
__c.LogImpl("81376259",_serviceid,0);
 //BA.debugLineNum = 180;BA.debugLine="If bStart = False Then";
if (_bstart==__c.False) { 
 //BA.debugLineNum = 181;BA.debugLine="manager.SetNotify(\"c1afc201-1fb5-459e-8fcc-c5c9";
_manager.SetNotify("c1afc201-1fb5-459e-8fcc-c5c9c331914b","c1a1d466-344c-4be3-ab3f-189f80dd7518",__c.True);
 //BA.debugLineNum = 182;BA.debugLine="manager.SetNotify(\"c1afc201-1fb5-459e-8fcc-c5c9";
_manager.SetNotify("c1afc201-1fb5-459e-8fcc-c5c9c331914b","c2a1d466-344c-4be3-ab3f-189f80dd7518",__c.True);
 //BA.debugLineNum = 183;BA.debugLine="bStart = True";
_bstart = __c.True;
 };
 //BA.debugLineNum = 186;BA.debugLine="Dim bc As ByteConverter";
_bc = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 189;BA.debugLine="For Each id As String In Characteristics.Keys";
{
final anywheresoftware.b4a.BA.IterableList group10 = _characteristics.Keys();
final int groupLen10 = group10.getSize()
;int index10 = 0;
;
for (; index10 < groupLen10;index10++){
_id = BA.ObjectToString(group10.Get(index10));
 //BA.debugLineNum = 192;BA.debugLine="If id = \"c1a1d466-344c-4be3-ab3f-189f80dd7518\"";
if ((_id).equals("c1a1d466-344c-4be3-ab3f-189f80dd7518")) { 
 //BA.debugLineNum = 193;BA.debugLine="choc = bc.HexFromBytes(Characteristics.Get(id)";
_choc = (float)(Double.parseDouble(_bc.HexFromBytes((byte[])(_characteristics.Get((Object)(_id))))));
 //BA.debugLineNum = 194;BA.debugLine="Log(choc)";
__c.LogImpl("81376275",BA.NumberToString(_choc),0);
 //BA.debugLineNum = 195;BA.debugLine="If choc = 0 Then";
if (_choc==0) { 
 //BA.debugLineNum = 196;BA.debugLine="Label3.Color = Colors.Green";
_label3.setColor(__c.Colors.Green);
 }else {
 //BA.debugLineNum = 198;BA.debugLine="Label3.Color = Colors.Red";
_label3.setColor(__c.Colors.Red);
 };
 }else if((_id).equals("c2a1d466-344c-4be3-ab3f-189f80dd7518")) { 
 //BA.debugLineNum = 203;BA.debugLine="Dim Data1() As Byte = Characteristics.Get(id)";
_data1 = (byte[])(_characteristics.Get((Object)(_id)));
 //BA.debugLineNum = 204;BA.debugLine="Log(Data1(0))";
__c.LogImpl("81376285",BA.NumberToString(_data1[(int) (0)]),0);
 //BA.debugLineNum = 205;BA.debugLine="dist = Data1(0)";
_dist = (float) (_data1[(int) (0)]);
 //BA.debugLineNum = 206;BA.debugLine="Log(dist)";
__c.LogImpl("81376287",BA.NumberToString(_dist),0);
 //BA.debugLineNum = 207;BA.debugLine="If dist > 8 Then";
if (_dist>8) { 
 //BA.debugLineNum = 208;BA.debugLine="Label4.Color = Colors.Green";
_label4.setColor(__c.Colors.Green);
 }else {
 //BA.debugLineNum = 210;BA.debugLine="Label4.Color = Colors.Red";
_label4.setColor(__c.Colors.Red);
 //BA.debugLineNum = 211;BA.debugLine="b.Beep";
_b.Beep();
 };
 };
 }
};
 };
 //BA.debugLineNum = 218;BA.debugLine="End Sub";
return "";
}
public String  _manager_devicefound(String _name,String _id,anywheresoftware.b4a.objects.collections.Map _advertisingdata,double _rssi) throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Sub Manager_DeviceFound (Name As String, Id As Str";
 //BA.debugLineNum = 154;BA.debugLine="Log(\"Found: \" & Name & \", \" & Id & \", RSSI = \" &";
__c.LogImpl("81245185","Found: "+_name+", "+_id+", RSSI = "+BA.NumberToString(_rssi)+", "+BA.ObjectToString(_advertisingdata),0);
 //BA.debugLineNum = 155;BA.debugLine="If Name = \"ESP32 BLE SERVER\" Then";
if ((_name).equals("ESP32 BLE SERVER")) { 
 //BA.debugLineNum = 156;BA.debugLine="ConnectedName = Name";
_connectedname = _name;
 //BA.debugLineNum = 157;BA.debugLine="manager.StopScan";
_manager.StopScan();
 //BA.debugLineNum = 158;BA.debugLine="Log(\"connecting\")";
__c.LogImpl("81245189","connecting",0);
 //BA.debugLineNum = 160;BA.debugLine="manager.Connect2(Id, True) 'disabling auto conne";
_manager.Connect2(_id,__c.True);
 };
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public String  _manager_disconnected() throws Exception{
 //BA.debugLineNum = 220;BA.debugLine="Sub Manager_Disconnected";
 //BA.debugLineNum = 221;BA.debugLine="Log(\"Disconnected\")";
__c.LogImpl("81441793","Disconnected",0);
 //BA.debugLineNum = 222;BA.debugLine="connected = False";
_connected = __c.False;
 //BA.debugLineNum = 223;BA.debugLine="StateChanged";
_statechanged();
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public String  _manager_statechanged(int _state) throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub Manager_StateChanged (State As Int)";
 //BA.debugLineNum = 141;BA.debugLine="Select State";
switch (BA.switchObjectToInt(_state,_manager.STATE_POWERED_OFF,_manager.STATE_POWERED_ON,_manager.STATE_UNSUPPORTED)) {
case 0: {
 //BA.debugLineNum = 143;BA.debugLine="currentStateText = \"POWERED OFF\"";
_currentstatetext = "POWERED OFF";
 break; }
case 1: {
 //BA.debugLineNum = 145;BA.debugLine="currentStateText = \"POWERED ON\"";
_currentstatetext = "POWERED ON";
 break; }
case 2: {
 //BA.debugLineNum = 147;BA.debugLine="currentStateText = \"UNSUPPORTED\"";
_currentstatetext = "UNSUPPORTED";
 break; }
}
;
 //BA.debugLineNum = 149;BA.debugLine="currentState = State";
_currentstate = _state;
 //BA.debugLineNum = 150;BA.debugLine="StateChanged";
_statechanged();
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public String  _startscan() throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Public Sub StartScan";
 //BA.debugLineNum = 168;BA.debugLine="If manager.State <> manager.STATE_POWERED_ON Then";
if (_manager.getState()!=_manager.STATE_POWERED_ON) { 
 //BA.debugLineNum = 169;BA.debugLine="Log(\"Not powered on.\")";
__c.LogImpl("81310722","Not powered on.",0);
 }else {
 //BA.debugLineNum = 171;BA.debugLine="manager.Scan2(Null, False)";
_manager.Scan2((anywheresoftware.b4a.objects.collections.List) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.collections.List(), (java.util.List)(__c.Null)),__c.False);
 };
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public String  _statechanged() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Public Sub StateChanged";
 //BA.debugLineNum = 57;BA.debugLine="lblState.Text = currentStateText";
_lblstate.setText(BA.ObjectToCharSequence(_currentstatetext));
 //BA.debugLineNum = 58;BA.debugLine="If connected Then";
if (_connected) { 
 //BA.debugLineNum = 59;BA.debugLine="lblDeviceStatus.Text = \"Connected: \" & Connected";
_lbldevicestatus.setText(BA.ObjectToCharSequence("Connected: "+_connectedname));
 }else {
 //BA.debugLineNum = 61;BA.debugLine="lblDeviceStatus.Text = \"Not connected\"";
_lbldevicestatus.setText(BA.ObjectToCharSequence("Not connected"));
 };
 //BA.debugLineNum = 63;BA.debugLine="btnDisconnect.Enabled = connected";
_btndisconnect.setEnabled(_connected);
 //BA.debugLineNum = 64;BA.debugLine="btnScan.Enabled = Not(connected)";
_btnscan.setEnabled(__c.Not(_connected));
 //BA.debugLineNum = 65;BA.debugLine="pbReadData.Hide";
_pbreaddata._hide /*String*/ ();
 //BA.debugLineNum = 66;BA.debugLine="pbScan.Hide";
_pbscan._hide /*String*/ ();
 //BA.debugLineNum = 67;BA.debugLine="btnReadData.Enabled = connected";
_btnreaddata.setEnabled(_connected);
 //BA.debugLineNum = 68;BA.debugLine="btnScan.Enabled = (currentState = manager.STATE_P";
_btnscan.setEnabled((_currentstate==_manager.STATE_POWERED_ON) && _connected==__c.False);
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public String  _uuid(String _id) throws Exception{
 //BA.debugLineNum = 234;BA.debugLine="Private Sub UUID(id As String) As String 'ignore";
 //BA.debugLineNum = 236;BA.debugLine="Return \"0000\" & id.ToLowerCase & \"-0000-1000-8000";
if (true) return "0000"+_id.toLowerCase()+"-0000-1000-8000-00805f9b34fb";
 //BA.debugLineNum = 240;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
if (BA.fastSubCompare(sub, "B4XPAGE_CREATED"))
	return _b4xpage_created((anywheresoftware.b4a.objects.B4XViewWrapper) args[0]);
return BA.SubDelegator.SubNotFound;
}
}
