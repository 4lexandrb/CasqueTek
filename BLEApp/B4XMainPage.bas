B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Class
Version=9.85
@EndOfDesignText@
#Region Shared Files
#CustomBuildAction: folders ready, %WINDIR%\System32\Robocopy.exe,"..\..\Shared Files" "..\Files"
'Ctrl + click to sync files: ide://run?file=%WINDIR%\System32\Robocopy.exe&args=..\..\Shared+Files&args=..\Files&FilesSync=True
#End Region

'Ctrl + click to export as zip: ide://run?File=%B4X%\Zipper.jar&Args=BLEExample.zip

Sub Class_Globals
	Private Root As B4XView
	Private xui As XUI
	Private btnReadData As B4XView
	Private btnDisconnect As B4XView
	Private btnScan As B4XView
	Private lblDeviceStatus As B4XView
	Private lblState As B4XView
	Private Label3 As B4XView
	Private Label4 As B4XView
	Private pbReadData As B4XLoadingIndicator
	Private clv As CustomListView
	#if B4A
	Private manager As BleManager2
	Private rp As RuntimePermissions
	#else if B4i
	Private manager As BleManager
	#end if
	Private currentStateText As String = "UNKNOWN"
	Private currentState As Int
	Private connected As Boolean = False
	Private ConnectedName As String
	Private ConnectedServices As List
	Private pbScan As B4XLoadingIndicator
	Private dist As Float = 0
	Private choc As Float = 0
	Private bStart As Boolean = False
	Dim Vibrate As PhoneVibrate
	Dim b As Beeper
	
	
End Sub

Public Sub Initialize
'	B4XPages.GetManager.LogEvents = True
	b.Initialize(300, 500)
	b.Beep
End Sub

'This event will be called once, before the page becomes visible.
Private Sub B4XPage_Created (Root1 As B4XView)
	Root = Root1
	Root.LoadLayout("1")
	B4XPages.SetTitle(Me, "BLE Example")
	manager.Initialize("manager")
	StateChanged
End Sub

Public Sub StateChanged
	lblState.Text = currentStateText
	If connected Then
		lblDeviceStatus.Text = "Connected: " & ConnectedName
	Else
		lblDeviceStatus.Text = "Not connected"
	End If
	btnDisconnect.Enabled = connected
	btnScan.Enabled = Not(connected)
	pbReadData.Hide
	pbScan.Hide
	btnReadData.Enabled = connected
	btnScan.Enabled = (currentState = manager.STATE_POWERED_ON) And connected = False
End Sub

Sub btnScan_Click
	#if B4A
	'Don't forget to add permission to manifest
	Dim Permissions As List
	Dim phone As Phone
	If phone.SdkVersion >= 31 Then
		Permissions = Array("android.permission.BLUETOOTH_SCAN", "android.permission.BLUETOOTH_CONNECT", rp.PERMISSION_ACCESS_FINE_LOCATION)
	Else
		Permissions = Array(rp.PERMISSION_ACCESS_FINE_LOCATION)
	End If
	For Each per As String In Permissions
		rp.CheckAndRequest(per)
		Wait For B4XPage_PermissionResult (Permission As String, Result As Boolean)
		If Result = False Then
			ToastMessageShow("No permission: " & Permission, True)
			Return
		End If
	Next
	#end if
	pbScan.Show
	StartScan
End Sub

Sub btnDisconnect_Click
	manager.Disconnect
	Manager_Disconnected
	bStart = False
End Sub

Sub btnReadData_Click
	pbReadData.Show
'	clv.Clear
	For Each s As String In ConnectedServices
		manager.ReadData(s)
	Next
End Sub

'Sub CreateServiceItem (service As String) As Panel
'	Dim pnl As B4XView = xui.CreatePanel("")
'	pnl.Color = 0xFF808080
'	pnl.SetLayoutAnimated(0, 0, 0, clv.AsView.Width, 30dip)
'	Dim lbl As B4XView = XUIViewsUtils.CreateLabel
'	lbl.Text = service
'	lbl.SetTextAlignment("CENTER", "CENTER")
'	lbl.Font = xui.CreateDefaultBoldFont(14)
'	pnl.AddView(lbl, 0, 0, clv.AsView.Width, 30dip)
'	Return pnl
'End Sub

'Sub CreateCharacteristicItem(Id As String, Data() As Byte) As Panel
'	Dim pnl As B4XView = xui.CreatePanel("")
'	pnl.SetLayoutAnimated(0, 0, 0, clv.AsView.Width, 40dip)
'	pnl.Color = Colors.White
'	Dim lbl As B4XView = XUIViewsUtils.CreateLabel
'	lbl.Text = Id
'	pnl.AddView(lbl, 0, 0, clv.AsView.Width, 20dip)
'	Dim lbl2 As B4XView = XUIViewsUtils.CreateLabel
'	Try
'		lbl2.Text = BytesToString(Data, 0, Data.Length, "UTF8")
'	Catch
'		Log(LastException)
'		lbl2.Text = "Error reading data as string"
'	End Try
'	lbl2.TextColor = 0xFF909090
'	lbl2.TextSize = 14
'	pnl.AddView(lbl2, 0, 20dip, clv.AsView.Width, 20dip)
'	Return pnl
'End Sub

Sub Manager_StateChanged (State As Int)
	Select State
		Case manager.STATE_POWERED_OFF
			currentStateText = "POWERED OFF"
		Case manager.STATE_POWERED_ON
			currentStateText = "POWERED ON"
		Case manager.STATE_UNSUPPORTED
			currentStateText = "UNSUPPORTED"
	End Select
	currentState = State
	StateChanged
End Sub

Sub Manager_DeviceFound (Name As String, Id As String, AdvertisingData As Map, RSSI As Double)
	Log("Found: " & Name & ", " & Id & ", RSSI = " & RSSI & ", " & AdvertisingData) 'ignore
	If Name = "ESP32 BLE SERVER" Then
		ConnectedName = Name
		manager.StopScan
		Log("connecting")
			#if B4A
		manager.Connect2(Id, True) 'disabling auto connect can make the connection quicker
		#else if B4I
		manager.Connect(Id)
		#end if
	End If
End Sub

Public Sub StartScan
	If manager.State <> manager.STATE_POWERED_ON Then
		Log("Not powered on.")
	Else
		manager.Scan2(Null, False)
	End If
End Sub

Sub Manager_DataAvailable (ServiceId As String, Characteristics As Map)
	If connected Then
		pbReadData.Hide
		Log(ServiceId)
		
		If bStart = False Then
			manager.SetNotify("c1afc201-1fb5-459e-8fcc-c5c9c331914b", "c1a1d466-344c-4be3-ab3f-189f80dd7518", True)
			manager.SetNotify("c1afc201-1fb5-459e-8fcc-c5c9c331914b", "c2a1d466-344c-4be3-ab3f-189f80dd7518", True)
			bStart = True
		End If

		Dim bc As ByteConverter
		
'		clv.Add(CreateServiceItem(ServiceId), "")
		For Each id As String In Characteristics.Keys
'			clv.Add(CreateCharacteristicItem(id, Characteristics.Get(id)), "")
			
			If id = "c1a1d466-344c-4be3-ab3f-189f80dd7518" Then
				choc = bc.HexFromBytes(Characteristics.Get(id))
				Log(choc)
				If choc = 0 Then
					Label3.Color = Colors.Green
				Else
					Label3.Color = Colors.Red
				End If
				
				
			Else If id = "c2a1d466-344c-4be3-ab3f-189f80dd7518" Then
				Dim Data1() As Byte = Characteristics.Get(id)
				Log(Data1(0))
				dist = Data1(0)
				Log(dist)
				If dist > 8 Then
					Label4.Color = Colors.Green
				Else
					Label4.Color = Colors.Red
					b.Beep
				End If
				
				
			End If
		Next
	End If
End Sub

Sub Manager_Disconnected
	Log("Disconnected")
	connected = False
	StateChanged
End Sub

Sub Manager_Connected (services As List)
	Log("Connected")
	connected = True
	ConnectedServices = services
	StateChanged
End Sub

'utility to convert short UUIDs to long format on Android
Private Sub UUID(id As String) As String 'ignore
#if B4A
	Return "0000" & id.ToLowerCase & "-0000-1000-8000-00805f9b34fb"
#else if B4I
	Return id.ToUpperCase
#End If
End Sub
