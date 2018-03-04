package services;

import static services.Constant.TYPE_FAVOURITES;
import static services.Constant.TYPE_RECIPE_ID;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/favourization")
@Produces(MediaType.APPLICATION_JSON)
public class RESTFavourizationService extends DatabaseAdapter {

	@GET
	@Path("/favourizedItems/{cookie}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserFavourites(@PathParam("cookie") String cookie){
		String selection = "`favourites`,`id`";
		String context = "cookie=\"" + cookie + "\"";
		String favourites = select(TYPE_FAVOURITES,"cookme.person", selection, context).toFavouritesString();
		return favourites;
	}
	
	@GET
	@Path("/recipeId/{recipeName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Integer getCurrentRecipeId(@PathParam("recipeName") String recipeName){
		String selection = "`id`";
		String context = "title=\"" + recipeName + "\"";
		Integer id = select(TYPE_RECIPE_ID,"cookme.recipe", selection, context).toRecipeId();
		return id;
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateFavourites( @FormParam("cookie") String cookie, @FormParam("favourites") String favourites ){
		String updateInformation = "favourites=\"" + favourites +"\"";
		String context = "cookie=\"" + cookie + "\"";
		Boolean done = update(TYPE_FAVOURITES,"cookme.person", updateInformation, context);
		if(done) { System.out.println("Favourites successfully updated."); }
		else { System.out.println("Failed updating favourites! cookie: " + cookie + " favourites: " + favourites); };
	}

	@PUT
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}