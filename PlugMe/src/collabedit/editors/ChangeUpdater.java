package collabedit.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.PlatformUI;

import collabedit.changes.EventHandler;
import collabedit.users.CollabUserConfig;
import collabedit.users.Collaborators;

public class ChangeUpdater implements IDocumentListener  {

	enum Actions {Add , Delete , Replace};
	
	
	@Override
	public void documentAboutToBeChanged(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private Actions FindAction(DocumentEvent DocEvent)
	{
		int DocLength = DocEvent.getLength();
		int DocOffset = DocEvent.getOffset();
		String DocText = DocEvent.getText();
		System.out.println(DocLength);
		System.out.println(DocOffset);
		System.out.println(DocText);
		if (DocLength == 0)
		{
			System.out.println("User added " + DocText);
			return Actions.Add;
		}
		
		else
		{
			if (DocText.equals("")) 
			{
				System.out.println("User deleted ");
				return Actions.Delete;
			}
			System.out.println("User Replaced with " + DocText);
			return Actions.Replace;
		}
		
		
		
	}
	
	@Override
	public void documentChanged(DocumentEvent event) {

		// TODO Auto-generated method stub
		System.out.println("Document changed");
		
		String DocumentName = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getTitle();
		String Act = FindAction(event).toString();
		System.out.println("My action := " + Act);
		EventHandler.SendEvent(EventHandler.GenerateEventFileChanged(new Change(DocumentName, CollabUserConfig.MyIP , event.getOffset() , event.getLength() , event.getText() , Act)),
								Collaborators.GetOnlineUsers());

	}

	public void DocumentUpdate(int DocOffset, int DocLength , String DocString , String Type) throws BadLocationException
	{
		System.out.println("External Event := " + Type);
		MainEditor.MainDoc.removeDocumentListener(this);
		if (Type.equals("Add"))
			MainEditor.MainDoc.replace(DocOffset, DocOffset, DocString);
		
		else if (Type.equals("Delete"))
			MainEditor.MainDoc.replace(DocOffset, DocOffset+DocLength, "");
		
		else if (Type.equals("Replace"))
			MainEditor.MainDoc.replace(DocOffset, DocOffset+DocLength, DocString);
		MainEditor.MainDoc.addDocumentListener(this);
	}
}
