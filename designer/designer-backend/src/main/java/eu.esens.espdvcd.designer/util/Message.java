package eu.esens.espdvcd.designer.util;

public class Message<E> {

    private E response;

    public Message() {
        this.response = null;
    }

    public E getResponse() {
        return response;
    }

    public void setResponse(E response){
        this.response = response;
    }

    public boolean checkResponse(){
        return response==null;
    }
}
