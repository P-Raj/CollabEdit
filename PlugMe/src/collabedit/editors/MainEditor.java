package collabedit.editors;

import java.sql.Timestamp;
import java.util.Date;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

import collabedit.changes.Event;
import collabedit.changes.EventHandler;
import collabedit.config.Config;
import collabedit.users.CollabUserConfig;
import collabedit.util.network.SetupNetworking;


@SuppressWarnings("restriction")
public class MainEditor extends JavaEditor implements IResourceChangeListener {

	public String CurrentUsername = null;
	public int CurrentUserID = 0;
	
	public boolean isSetupEditor = false;
	static public TextEditor editor;
	
	private Date DATE;
	
	public static IDocument MainDoc ;
	public static ChangeUpdater MainDocUpdater;

	public MainEditor edt = null;
	
	public MainEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		
		
		SetupNetworking.Setup();
				
		System.out.println("Waiting for my IP Address");
		
		while(true){
			if (CollabUserConfig.IsUserSet) break;
			System.out.print(""); // DANGER: DO NOT REMOVE THIS PRINT STATEMENT
	    }
		
		
		CurrentUsername = CollabUserConfig.MyIP;
		System.out.println("IP Address Set !");
		
		//wait to check whether I am Master or not ?
		Timestamp StartTime = GetCurrentTime();
		
		while (true)
		{
			if (GetCurrentTime().getTime() - StartTime.getTime() > Config.IAmServerLatenncy){
				CollabUserConfig.AmIHost = true;
				break;
			}
			
			if (CollabUserConfig.MasterIP != null){
				CollabUserConfig.AmIHost = false;
				break;
			}
			
		}
		
		System.out.println("AmIhost : " + CollabUserConfig.AmIHost);
		System.out.println("MasterIP : " + CollabUserConfig.MasterIP);
		
		
		
		SetupEditor();
	}
	
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	
	@Override
	protected IJavaElement getCorrespondingElement(IJavaElement arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected IJavaElement getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
	
	
	public Timestamp GetCurrentTime() {
		
		DATE = new java.util.Date();
		return (new Timestamp(DATE.getTime()));
	}
	
	
	public static void UpdateEditor(Event event) throws BadLocationException
	{
		MainEditor.MainDoc.removeDocumentListener(MainEditor.MainDocUpdater);
		
		//Showing that change in editor
		int a = 0 , b = 0;
		String c;
		
		if (event.FileChange.Type.equals("Add"))
		{
			a = event.FileChange.ChangeOffset;
			c =  event.FileChange.ChangeText;
			System.out.println("Adding " + c + " at position " + a);
			MainEditor.MainDoc.replace(a , a, c);
		}
		else if (event.FileChange.Type.equals("Delete"))
		{
			a = event.FileChange.ChangeOffset;
			b = event.FileChange.ChangeOffset + event.FileChange.ChangeLength;
			System.out.println("Deleting from " + a + " to " + b);
			MainEditor.MainDoc.replace(a,b,"");
		}
		else if (event.FileChange.Type.equals("Replace"))
		{
			a = event.FileChange.ChangeOffset;
			b = event.FileChange.ChangeOffset + event.FileChange.ChangeLength;
			c =  event.FileChange.ChangeText;
			System.out.println("Replaced with " + c);
			MainEditor.MainDoc.replace(a,b,c);
		}
		System.out.println("From " + a + " to " + b);
		
		MainEditor.MainDoc.addDocumentListener(MainEditor.MainDocUpdater);
	}
	
	
	private void SetupEditor(){
		
		editor = new TextEditor();
		//int index = addPage(editor, getEditorInput());
		//setPageText(index, editor.getTitle());
		if (editor == null)
			System.out.println("BUG : editor = null");
		else{
			System.out.println("editor!=null");
			
			IDocumentProvider dp = this.getDocumentProvider();
			while (this.getDocumentProvider() == null);
			MainDoc = this.getDocumentProvider().getDocument(this.getEditorInput());
			//MainDoc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			System.out.println("editor!=null");
			MainDocUpdater = new ChangeUpdater();
			MainDoc.addDocumentListener(MainDocUpdater);
			System.out.println("editor!=null");
			
			//Event generation : File Open
			EventHandler.GenerateEventFileOpen(editor.getTitle());
		}
	}

}
