package controller;

import dao.UserDAO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;



@WebServlet(name = "Usuarios", urlPatterns = {"/UserController"})
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/user.jsp";
    private static String LIST_USER = "/listUser.jsp";
    private UserDAO dao;

    public MainServlet() {
        super();
        dao = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String forward="";
       String action = request.getParameter("action");
       if(action == null) {

           RequestDispatcher view = request.getRequestDispatcher("/user.jsp");
           view.forward(request, response);
           return;
       }

       if (action.equalsIgnoreCase("delete")){
           int userId = Integer.parseInt(request.getParameter("userId"));
           dao.deleteUser(userId);
           forward = LIST_USER;
           request.setAttribute("users", dao.getAllUsers());    
       } else if (action.equalsIgnoreCase("edit")){
           forward = INSERT_OR_EDIT;
           int userId = Integer.parseInt(request.getParameter("userId"));
           User user = dao.getUserById(userId);
           request.setAttribute("user", user);
       } else if (action.equalsIgnoreCase("listUser")){
           forward = LIST_USER;
           request.setAttribute("users", dao.getAllUsers());
       } else {
           forward = INSERT_OR_EDIT;
       }

       RequestDispatcher view = request.getRequestDispatcher(forward);
       view.forward(request, response);
   }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        try {
            Date dob=null;
            String teste = request.getParameter("dob");
            System.out.println(teste);
            if(request.getParameter("dob")!=null){
                java.util.Date d = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dob"));
                dob = new Date(d.getTime());
            }
            else{
                dob = null;
            }
                
            user.setDob(dob);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setEmail(request.getParameter("email"));
        String userid = request.getParameter("userid");
        if(userid == null || userid.isEmpty())
        {
            dao.addUser(user);
        }
        else
        {
            user.setUserid(Integer.parseInt(userid));
            dao.updateUser(user);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("users", dao.getAllUsers());
        view.forward(request, response);
    }
}