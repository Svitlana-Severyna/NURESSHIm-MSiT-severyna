package Wumpus;

import aima.core.agent.Action;
import aima.core.environment.wumpusworld.HybridWumpusAgent;
import aima.core.environment.wumpusworld.WumpusAction;
import aima.core.environment.wumpusworld.WumpusPercept;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class NavigatorAgent extends Agent {

    private HybridWumpusAgent HybridAgent = new HybridWumpusAgent();

    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Navigator");
        sd.setName("MyNavigator");
        dfd.addServices(sd);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("I'm ready");
        addBehaviour(new OfferRequestsServer());
    }

    private class OfferRequestsServer extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            System.out.println("I'm here");
            if (msg != null) {
                String title = msg.getContent();
                WumpusPercept wp = StringToPercept(title);
                Action spelAction = HybridAgent.execute(wp);
                System.out.println(spelAction.toString());

                DFAgentDescription templateSpel = new DFAgentDescription();
                ServiceDescription sdSpel = new ServiceDescription();
                sdSpel.setType("Speleilogist");
                templateSpel.addServices(sdSpel);

                ACLMessage reply = msg.createReply();

                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(spelAction.toString());
                myAgent.send(reply);
            }
            myAgent.doWait(2000);
        }
    }

    private WumpusPercept StringToPercept (String s)
    {
        WumpusPercept result = new WumpusPercept();
        if (s.contains("Stench"))
            result.setStench();
        if (s.contains("Breeze"))
            result.setBreeze();
        if(s.contains("Bump"))
            result.setBump();
        if (s.contains("Glitter"))
            result.setGlitter();
        if (s.contains("Scream"))
        result.setScream();
        return result;

    }
}
