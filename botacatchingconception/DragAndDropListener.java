package com.example.botacatchingconception;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.db.bdd.BddQube;

class DragAndDropListener implements OnDragListener 
{
	private Activity activity;
	private ViewGroup parent;
	private DisplayMetrics metrics;
	
	private int scrollRightPosition;
	private int scrollLeftPosition;
	private int scrollOffset;
	private ImageView barre;
	private int indexBegin;
	
	public int idNiveau;
	public int sizeViewMoved;
	public static boolean firstTime = true;
	public static boolean dragAndDropEnable = false;
	
	public DragAndDropListener(Activity activity, ViewGroup parent) 
	{
		super();
		this.activity = activity;
		this.parent = parent;
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.scrollLeftPosition = 0;
        this.scrollRightPosition = metrics.widthPixels;
        this.scrollOffset = metrics.widthPixels / 5;
        barre = new ImageView(activity);
        barre.setPadding(0, 0, metrics.widthPixels/25, 0);
        barre.setImageResource(R.drawable.blueline);
	}

	@Override
	public boolean onDrag(final View v, final DragEvent event) 
	{
		// Handles each of the expected events
	    switch (event.getAction()) 
	    {
	      case DragEvent.ACTION_DRAG_LOCATION:
	    	  if(dragAndDropEnable && v.getClass() == LinearLayout.class) // NIVEAU LAYOUT
			  {
	    		int index = (int)event.getX() / sizeViewMoved;
	    		LinearLayout container = (LinearLayout) v;
	    		if(container.indexOfChild(barre) != -1)
	    			container.removeView(barre);
	    		
	    		if(index != indexBegin && index != indexBegin + 1)
	    		{
		    		if(container.getChildCount() < index)
						container.addView(barre);
					else
						container.addView(barre, index);
	    		}
	    		
	    		
	    	   	if(scrollRightPosition <= event.getX() + scrollOffset)
	    	   	{
	    	   		final HorizontalScrollView horizontalScrollView = (HorizontalScrollView)v.getParent();	
	    	   		horizontalScrollView.scrollBy(50, 0);
	    	   		scrollRightPosition += 50;
	    	   		scrollLeftPosition += 50;
	    	   	}
	    	   	else if(event.getX() - scrollOffset <= scrollLeftPosition)
	    	   	{
	    	   		final HorizontalScrollView horizontalScrollView = (HorizontalScrollView)v.getParent();	
	    	   		horizontalScrollView.scrollBy(-50, 0);
	    	   		scrollRightPosition -= 50;
	    	   		scrollLeftPosition -= 50;
	    	   	}
			  } 	
	    		break;
	      case DragEvent.ACTION_DRAG_STARTED:
	      {
    		  View view = (View) event.getLocalState();

	    	  if(view != null && dragAndDropEnable && v.getClass() == LinearLayout.class) // NIVEAU LAYOUT
	    	  {
				  LinearLayout container = (LinearLayout) v;
				  indexBegin = container.indexOfChild((View)view.getParent());
	    	  }
	    	  
	    	  return true; 
	      }
	    	 
	      case DragEvent.ACTION_DRAG_ENTERED:
	      {
	    	//La view que l'on bouge est entree sur la view sur laquelle on a ajouté le listener
	    	View view = (View) event.getLocalState();
	    	if(view != null)
	    		view.setVisibility(View.INVISIBLE);
	        break;
	      }
	      case DragEvent.ACTION_DRAG_EXITED:
	      {
		    //La view que l'on bouge a quitté la view sur laquelle on a ajouté le listener
//	    	View view = (View) event.getLocalState();
//			if(view != null)
//				view.setVisibility(View.VISIBLE);
			
	        break;
	      }
	      case DragEvent.ACTION_DROP:
	      {
	    	View view = (View) event.getLocalState();
	    	if(view != null)	//Si l'on est sur la view sur laquelle on veut drop l'annotation
			{
	    		if(dragAndDropEnable && v.getClass() == ImageView.class)
				{
	    			ViewGroup viewgroup = (ViewGroup) view.getParent();
	
	    			if(event.getX()- ZoneObservationLayout.imageZoneObs.getWidth()/12 >= 0 &&
			    		 event.getX() <= ZoneObservationLayout.imageZoneObs.getWidth() - ZoneObservationLayout.imageZoneObs.getWidth()/12)
	    			{
						  ZoneObservationLayout.validationButton.setEnabled(true);
				    	  viewgroup.removeView(view);
				    	  ((ViewGroup) this.parent.getParent().getParent()).addView(view);
				    	  
				    	  if(firstTime)
				    	  {
				    		  view.setX(event.getX()-(ZoneObservationLayout.imageZoneObs.getWidth()/2));
				    		  view.setY(event.getY()+(ZoneObservationLayout.imageZoneObs.getHeight()/5*3)); 
				    		  firstTime = false;
				    	  }
				    	  else
				    	  {
				    		  view.setX(event.getX()-(ZoneObservationLayout.imageZoneObs.getWidth()/2));
				    		  view.setY(event.getY()+(ZoneObservationLayout.imageZoneObs.getHeight()/3));  
				    	  }
				    	  
				    	  view.setVisibility(View.VISIBLE);
					  }
				  	} 
				  	else if(dragAndDropEnable && v.getClass() == LinearLayout.class) // NIVEAU LAYOUT
				  	{
				  		ViewGroup scroll = (ViewGroup) view.getParent();
				  		parent.removeView(scroll);
				  		//owner.removeView(view);
				  		LinearLayout container = (LinearLayout) v;
				  		int width = view.getWidth();
				  		int positionX = (int)event.getX();
				  		int index = positionX/width;
					  
				  		if(container.getChildCount() < index)
				  			container.addView(scroll);
				  		else
				  			container.addView(scroll, index);
				  	}
				  	else if(v instanceof EditText)
				  	{
				  		Toast.makeText(activity, "fu", Toast.LENGTH_SHORT).show();
				  		ZoneObservationLayout.validationButton.setEnabled(false);
				  		ViewGroup viewgroup = (ViewGroup) view.getParent();
				  		viewgroup.removeView(view);		    	  
				  		view.setX(0);
				  		view.setY(0);
				  		parent.addView(view, parent.getChildCount() - 1);
				  	}		
				  	else 
				  	{
				  		if(ZoneObservationLayout.validationButton != null)
				  		{
				  			ZoneObservationLayout.validationButton.setEnabled(false);
				  			ViewGroup viewgroup = (ViewGroup) view.getParent();
				  			if(viewgroup != parent)
				  			{
				  				viewgroup.removeView(view);		    	  
				  				view.setX(0);
				  				view.setY(0);
				  				parent.addView(view, parent.getChildCount() - 1);
				  			}
				    	  
				  			firstTime = true; 	  
				  			Toast.makeText(activity, "Vous ne pouvez pas poser l'annotation ici", 
				  					Toast.LENGTH_SHORT).show();
				  		}
				  	}	 
	    			view.setVisibility(View.VISIBLE);
	      	  }
	    	  break;
	      }
	      case DragEvent.ACTION_DRAG_ENDED:
	      {
	    	  v.post(new Runnable()
	    	  {
	    		  @Override
	              public void run() 
	    		  {
	    			  View view = (View) event.getLocalState();
	    		  	  if(dragAndDropEnable && v.getClass() == LinearLayout.class) // NIVEAU LAYOUT
	    			  {
	    		  		  LinearLayout container = (LinearLayout) v;
	    		  		  container.removeView(barre);
	    		  		  BddQube bddQube = new BddQube(activity);
	    		  		  bddQube.open();
	    		  		  bddQube.updateOrderQube(idNiveau, indexBegin, container.indexOfChild(view));
	    		    		
	    		  		  bddQube.close();
	    			  }
	    	    	  if (view != null)
		    	    	  view.setVisibility(View.VISIBLE);
	    		  }
	    	  });
	    	  break;
	      }
	      default:
	        break;
	    }
	    
	    return true;
	}
}