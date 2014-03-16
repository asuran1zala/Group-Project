package cs.ualberta.ca.tunein;

import cs.ualberta.ca.tunein.network.ElasticSearchOperations;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Controller
 * CommentController class:
 * This is a controller class that manipulates the comment model. The main purpose
 * of this class is when a view needs to modify anything that is related to a model.
 * To use this controller create a new CommentController object with a comment
 * and using that controller modify the comment sent to the controller.
 */
public class CommentController{

	private Comment comment;
	
	/**
	 * Constructor to construct a controller that can modify comments.
	 * @param aComment The comment that the controller modifies.
	 */
	public CommentController(Comment aComment) 
	{
		this.comment = aComment;
	}

	/**
	 * Method to edit the text of a comment.
	 * @param text The edited text.
	 */
	public void editText(String text) {
		comment.setComment(text);
	}

	/**
	 * Method to change the location of a comment.
	 * @param lon The longitude.
	 * @param lat The latitude.
	 */
	public void changeLoc(Double lon, Double lat) {
		comment.getGeolocation().setLongitude(lon);
		comment.getGeolocation().setLatitude(lat);
	}

	/**
	 * Method to add an image to comment.
	 * @param img The image. to be added.
	 */
	public void addImg(Image img) {
		comment.setImg(img);
	}
	
	/**
	 * Method to create a reply to a comment with image.
	 * @param currentComment The topic comment that the reply will belong to.
	 * @param act The activity that calls this method.
	 * @param title The title of the comment.
	 * @param text The text of the comment.
	 * @param img The image of the comment.
	 * @param isReply Check if the added comment will be reply of reply.
	 */
	public void addReplyImg(Comment currentComment, Activity act, String title, String text, Image img, boolean isReply) {
		
		UserController userCntrl = new UserController();
    	String username = userCntrl.loadUsername(act);
    	String id = userCntrl.loadUserid(act);
		Commenter user = new Commenter(username, id);
		
		GeoLocation loc = new GeoLocation();
		GeoLocationController geoCntrl = new GeoLocationController(loc);
		geoCntrl.getLocation(act);
		
		Comment aComment = new Comment(user, title, text, loc, img);
		comment.addReply(aComment);
		comment.increaseReplyCount();
		
		if(isReply)
		{
			aComment.increaseReplyCount();
		}
		
		ElasticSearchOperations.putCommentModel(currentComment);
	}
	
	
	/**
	 * Method to create a reply to a comment with image.
	 * @param currentComment The topic comment that the reply will belong to.
	 * @param act The activity that calls this method.
	 * @param title The title of the comment.
	 * @param text The text of the comment.
	 * @param isReply Check if the added comment will be reply of reply.
	 */
	public void addReply(Comment currentComment, Activity act, String title, String text, boolean isReply) {
		
		UserController userCntrl = new UserController();
    	String username = userCntrl.loadUsername(act);
    	String id = userCntrl.loadUserid(act);
		Commenter user = new Commenter(username, id);
		
		GeoLocation loc = new GeoLocation();
		GeoLocationController geoCntrl = new GeoLocationController(loc);
		geoCntrl.getLocation(act);
		
		Comment aComment = new Comment(user, title, text, loc);
		comment.addReply(aComment);
		comment.increaseReplyCount();
		
		if(isReply)
		{
			aComment.increaseReplyCount();
		}
		
		ElasticSearchOperations.putCommentModel(currentComment);
	}

	/**
	 * Method of adding comment to cache.
	 * @param aComment The comment to be added.
	 */
	public void addtoCache(Comment aComment) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Method of adding comment to favorites.
	 * @param aComment The comment to be added.
	 */
	public void favorite(Comment aComment) {
		comment.increaseFavCount();
	}

	/**
	 * Method of editing a comments title.
	 * @param text The new title.
	 */
	public void editTitle(String text) {
		comment.setTitle(text);
	}
	
	/**
	 * Method to check if the current user is the comment author
	 * this is used to check credentials.
	 * @param act Activity that this method is called from.
	 * @return The resulting boolean of the check.
	 */
	public boolean checkValid(Activity act) {
		//id of the current viewer
		SharedPreferences prefs = act.getSharedPreferences(
			      "cs.ualberta.ca.tunein", Context.MODE_PRIVATE);
		String currentID = prefs.getString("cs.ualberta.ca.tunein.userid", "");
		return comment.getCommenter().getUniqueID().equals(currentID);
	}
	
	public void updateElasticSearch(Comment aComment)
	{
		ElasticSearchOperations.putCommentModel(aComment);
	}
}
