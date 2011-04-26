package com.nmt.nmj.editor;

/**
 * Interface defining the application's command IDs. Key bindings can be defined
 * for specific commands. To associate an action with a command, use
 * IAction.setActionDefinitionId(commandId).
 * 
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_OPEN_DATABASE = "nmj.editor.openDatabase";
    public static final String CMD_REFRESH_DATABASE = "nmj.editor.refreshDatabase";
    public static final String CMD_CLOSE_DATABASE = "nmj.editor.closeDatabase";
    public static final String CMD_LIST_VIEW = "nmj.editor.listView";
    public static final String CMD_WALL_VIEW = "nmj.editor.wallView";

}