package net.mineshaft.data;

import java.util.ArrayList;

public class UserCacheClass {
    ArrayList<String> users = new ArrayList<>();

    public ArrayList<String> getUsers() {return users;}
    public void setUsers(ArrayList<String> newUsers) {this.users=newUsers;}
    public void addUser(String user) {if(!users.contains(user)) {this.users.add(user);}}
    public void removeUser(String user) {this.users.remove(user);}
    public void clearUsers() {this.users.clear();}
}
