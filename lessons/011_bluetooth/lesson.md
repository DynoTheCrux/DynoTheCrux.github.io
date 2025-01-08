# Overview
{: .reading}

* This will become a table of contents (this text will be scrapped).
{:toc}



# Workshop: Bluetooth Chat using a Android and Arduino

This guide explains how to set up and work with the provided Android Studio template to create a smartphone app to chat with your ESP32! It includes the Bluetooth client implementation and integrates it with an ESP32 using JSON serialization. 

---

## Get the Template

Start by opening the provided Android Studio template project. Here's an overview of the project's structure:

### Project Structure
The project is organized into three main packages:

1. **Connection**
   - Contains the Bluetooth client and interface classes.
   - Manages the connection to the ESP32 and communication protocols.

2. **Data**
   - Defines the structure of the data being sent and received via Bluetooth.

3. **Util**
   - Includes utility classes and methods to simplify common operations related to Bluetooth

4. **MainActivity**
   - Includes the application flow and UI. Implements the `ConnectionListener` to implement Bluetooth methods.
  
In the next steps you will implement the methods of the `ConnectionListener`. Generally, it is structured in a way to easily use and include Bluetooth serial in your (future) projects. Hence, this lesson is not going into the needed detail to create a custom solution on your own.

---

## The Connection Package

In the connection package three classes can be found:

1. `AbstractClientConnection`:  Serves as the base class for `BluetoothClient`handling the logic for background work using threads.

2. `BluetoothClient`: Extends the `AbstractClientConnection`. It specifies the serial port profile, creates the bluetooth adapter and gets the system level service from the android OS. Via the `socket` input and output data can be accessed.

3. `ConnectionListener`: The interface to interact with the bluetooth classes. It is used in the `AbstractCLientConnection` and the objects stored in a list. This allows to have more than one activity using the same bluetooth connection throughout your application.

## Implement Bluetooth Connection Listener Methods

To handle the Bluetooth connection, implement the listener methods in the `ConnectionListener` interface.

Integrate the Bluetooth connection and data handling in the `MainActivity`. Therefore your `MainActivity` has to implement the interface `ConnectionListener` using the `implements` keyword.

In cosequence we are forced to implement the follwing methods:

```java
    void onConnected();
    void onConnectionFailed(Throwable t);
    void onDisconnected();
    void onMessageReceived(String message);
```

What do we want to do in each? We want to:
1. Allow sending messages if the connection is established and prohibit it if not. You can implement that logic in a few ways. One idea could be to disable and enable the button when applicable.
2. Try to reconnect to the device if the connection failed or is lost. The following code snippets can be used:

````java
    DeviceDiscoverer.stopSearch(); // this line is only needed if the connection fails
    DeviceDiscoverer.searchDevice(btDevice -> {
        getConnection().connect(btDevice.getAddress(), 1);
        DeviceDiscoverer.stopSearch();
    });

````
````java


    public AbstractClientConnection getConnection(){
      if(connection == null){
          this.connection = new BluetoothClient(((BluetoothManager)    this.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter());
          this.connection.addConnectionListener(this);
      }
      return connection;
    }
````


4. Show the message if there is a new one. To do that we need to deserialize the JSON message. The methods for de/serialization is already provided in the project and can be used the following:

````java
    try {
        ChatMessage msg = deserializeMessage(message);
        runOnUiThread(() -> listAdapter.add(msg));
    } catch (JSONException e) {
        throw new RuntimeException(e);
    }
````

> Make sure you are using `runOnUIThread` to interact with UI elements in the methods of the Connection Listener!
 
Sending a message will be done through a button press later on.

---

## Implement the `onCreate()` logic

After the permissions were requested, we first want to search for a device and connect to it. Therefore you can use the following code snippet. Make sure to set the correct `DEVICE_NAME` to find the ESP32 you want to connect to.

````java
    DeviceDiscoverer = new DeviceNameDiscoveryUtil(this, DEVICE_NAME);
    
    DeviceDiscoverer.searchDevice(btDevice -> {
        getConnection().connect(btDevice.getAddress(), 1);
        DeviceDiscoverer.stopSearch();
    });
````

Last thing that we need to do is to create a `onClickListener` for the sending button and send the new message. In the listener we first create the message, add it to the UI, serialize it and send it:

````java
    ChatMessage msg = new ChatMessage(MY_USER_NAME, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), txtMessageInput.getText().toString());
    listAdapter.add(msg);
    
    //serialize message before sending
    try {
        String jsonMessage = serializeMessage(msg);
        getConnection().sendMessage(jsonMessage);
    
    } catch (JSONException je) {
        Log.e("JSON", "Error while serializing message", je);
    }
    
    txtMessageInput.setText("");

```` 

---

## Testing with ESP32

To test the application, use the provided ESP32 code to receive and process JSON data. Flash the ESP32 with the code and ensure it is discoverable via Bluetooth.
