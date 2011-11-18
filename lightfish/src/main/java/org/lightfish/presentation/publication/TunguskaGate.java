package org.lightfish.presentation.publication;

import java.io.IOException;
import java.util.logging.Logger;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@WebServlet(name = "TunguskaGate", urlPatterns = {"/live"}, asyncSupported = true)
public class TunguskaGate extends HttpServlet {

    @Inject
    Event<BrowserWindow> events;
    private final static Logger LOG = Logger.getLogger(TunguskaGate.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AsyncContext startAsync = request.startAsync();
        events.fire(new BrowserWindow(startAsync));
        LOG.info("Event sent");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
