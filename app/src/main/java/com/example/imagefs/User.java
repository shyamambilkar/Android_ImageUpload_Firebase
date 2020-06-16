package com.example.imagefs;

public class User {
    public int status;
    public String filename;
    public String name;


    public User(int status,String filename,String name){
    this.status=status;
    this.filename=filename;
    this.name=name;
    }

    public User(){

    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getFilename(){
        return filename;
    }
    public void setFilename(String filename){
        this.filename=filename;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status){
        this.status=status;
    }
}
