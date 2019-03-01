package com.krypt0n.kara.Cloud

import com.krypt0n.kara.Repository.filesDirectory
import com.krypt0n.kara.Repository.ftpConnected
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.*

/**
 *  Cloud backup that use FTP for remote file upload and download
 */
object Cloud {
    //user folder on server
    private val userFolder = "/Documents/Kara/${Account.email}"
    //ftp client for connecting
    private val client = FTPClient()
    //connect to server FTP
    private fun connectFTP() {
        client.apply {
            connect("192.168.0.13",2121)
            login("krypt0n", "1708")
            enterLocalPassiveMode()
            //will navigate to users folder
            changeWorkingDirectory(userFolder)
            setFileType(FTP.BINARY_FILE_TYPE)
        }
    }
    //create user directory
    private fun createDirectory(){
        client.makeDirectory(userFolder)
    }
    //check if user directory exist
    private fun userFolderExist() : Boolean {
        if (client.changeWorkingDirectory(userFolder))
            return true
        return false
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
     * uploadFile backup to cloud
     * @param fileName
     * @param fileForUpload
     */
    @Synchronized
    fun uploadFile(fileName : String, fileForUpload : String) {
        try{
            connectFTP()
            if (File(fileForUpload).exists()) {
                //create user folder if missing
                if (!userFolderExist())
                    createDirectory()
                //uploadFile procedure
                client.apply {
                    //uploadFile backup to server
                    storeFile(fileName,BufferedInputStream(FileInputStream(fileForUpload)))
                }
            }
        } catch (e: Exception) {
            val f = File("$filesDirectory/log.txt")
            val p = PrintStream(f)
            e.printStackTrace(p)
        } finally {
            //in any case will disconnect
            client.disconnect()
        }
    }
    /**
     * restore backup from cloud
     * @param fileName
     * @param fileLocation
     */
    @Synchronized
    fun syncFile(fileName : String, fileLocation : String) {
        try {
            connectFTP()
            //check if backup exist
            if (backupExist("$userFolder/$fileName")) {
                //downloading file to app folder
                client.retrieveFile(fileName,FileOutputStream(fileLocation))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            //in any case will disconnect
            client.disconnect()
        }
    }
}