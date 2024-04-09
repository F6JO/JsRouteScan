package ui.Interface;

import core.Interface.ContentClass;

public interface JXTableInterfae<T> {

    public Object add(Object a);
    public void remove(String a);
    public Object find(String a);
    public void clear();
    public void updateAll();
    public void Select_Change_Listener();
    public void Select_RightClick_Listener();
    public T getSelected();


}
