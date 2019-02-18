package com.krypt0n.kara.Cloud

import com.krypt0n.kara.Repository.internetAvailable
import org.apache.commons.net.ftp.FTPClient
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/* Cloud.Cloud backup that use FTP for remote file upload and download */
class Cloud(val location : String) {
    private lateinit var ftpFolder : String    //user folder on server
	private val client = FTPClient()     //ftp client for connecting
    private val ip = "192.168.0.11"  //server ip
    private val port = 2221                       //connection port
    private var ftpConnected = false

    //check if user folder on server exist
	private fun ftpDirectoryExist(dirPath : String) : Boolean{
        if (ftpConnected) {
            //go to user folder
            client.changeWorkingDirectory(dirPath)
            val returnCode = client.replyCode
            if (returnCode == 550) {
                return false
            }
        }
        return true
    }
    //check if buckup file on server exist
    private fun backupExist(filePath : String) : Boolean {
        if (ftpConnected) {
            val inputStream = client.retrieveFileStream(filePath)
            val returnCode = client.getReplyCode();
            if (inputStream == null || returnCode == 550) {
                return false
            }
        }
        return true
    }
    //connect to server via ftp
    fun ftpConnect() {
        if (internetAvailable) {
            try {
                client.connect(ip, port)
                client.login("francis", "francis")
                ftpConnected = true
            } catch (e : IOException) {
            }
        }
    }
    //make backup
    fun upload(type : String) {
        //background thread for uploading
        Thread {
            try {
                if (ftpConnected) {
                    if (!ftpDirectoryExist(ftpFolder)) {
                        //create folder if missing
                        client.makeDirectory(ftpFolder)
                    }
                    //upload
                    client.storeFile(type,FileInputStream(location + type))
                }
            } catch (e : IOException) {}
        }.start()
    }
    fun sync(type : String) {
        try {
            //check if buckup exist
            if (ftpConnected){
                if(backupExist(ftpFolder + type)) {
                    client.changeWorkingDirectory(ftpFolder)
                    //downloading file to app folder
                    client.retrieveFile(type,FileOutputStream(location + type))
                    /*TODO
                    * add snackbar*/
                }
            }
        } catch (e : IOException) {}
    }
}