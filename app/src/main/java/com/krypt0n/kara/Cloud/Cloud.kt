package com.krypt0n.kara.Cloud

import com.krypt0n.kara.Repository.ftpConnected
import com.krypt0n.kara.Repository.ip
import org.apache.commons.net.ftp.FTPClient
import java.io.*

/**
 *  Cloud backup that use FTP for remote file upload and download
 */
object Cloud {
    //user folder on server
    private val ftpFolder = "Documents/Kara/${Account.name}/"
    //ftp client for connecting
    private val client = FTPClient()

    private fun connectFTP() {
        client.apply {
            connect(ip)
            login("krypt0n", "1708")
            enterLocalPassiveMode()
        }
    }
    private fun disconnectFTP(){
        client.apply {
            logout()
            disconnect()
        }
    }
    //create user directory
    private fun createDirectory(){
        client.makeDirectory(ftpFolder)
    }
    //check if backup file on server exist
    private fun backupExist(filePath : String) : Boolean {
        if (ftpConnected) {
            val inputStream = client.retrieveFileStream(filePath)
            if (inputStream == null || client.replyCode == 550) {
                return false
            }
        }
        return true
    }
    /**
     * upload backup to cloud
     * @param fileName
     * @param fileForUpload
     */
    fun upload(fileName : String,fileForUpload : String) {
        //check if user directory exist
        fun userFolderExist() : Boolean {
            if (client.changeWorkingDirectory(ftpFolder))
                return true
            return false
        }
        //upload thread
        Thread {
            try{
                if (File(fileForUpload).exists()) {
                    //connectFTP to server ftp
                    connectFTP()
                    //create user folder if missing
                    if (!userFolderExist())
                        createDirectory()
                    //upload procedure
                    client.apply {
                        changeWorkingDirectory(ftpFolder)
                        //upload backup to server
                        storeFile(fileName, FileInputStream(fileForUpload))
                    }
                    disconnectFTP()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * restore backup from cloud
     * @param file
     * @param fileLocation
     */
    fun sync(file : String,fileLocation : String) {
        try {
            //check if backup exist
            if (backupExist(ftpFolder + file)) {
                //retrieve backup from cloud
                client.apply {
                    changeWorkingDirectory(ftpFolder)
                    //downloading file to app folder
                    retrieveFile(file,BufferedOutputStream(FileOutputStream(fileLocation)))
                }
            }
        } catch (e : IOException) {}
    }
}