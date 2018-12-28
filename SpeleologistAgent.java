package Wumpus;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SpeleologistAgent extends Agent {

    private String message;
    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Speleilogist");
        sd.setName("MySpeleilogist");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        addBehaviour(new Conversation());
    }

    private class Conversation extends Behaviour
    {
        @Override
        public void action() {
            DFAgentDescription templateEnv = new DFAgentDescription();
            ServiceDescription sdEnv = new ServiceDescription();
            sdEnv.setType("Enviroment");
            templateEnv.addServices(sdEnv);

            DFAgentDescription templateNav = new DFAgentDescription();
            ServiceDescription sdNav = new ServiceDescription();
            sdNav.setType("Navigator");
            templateNav.addServices(sdNav);

            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
            cfp.setConversationId("env");
            try {
                DFAgentDescription[] result = DFService.search(myAgent, templateEnv);
                cfp.addReceiver(result[0].getName());
            } catch (FIPAException e) {
                e.printStackTrace();
            }
            cfp.setContent(message);

            cfp.setReplyWith("cfp"+System.currentTimeMillis());
            myAgent.send(cfp);
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("env"),
                    MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
            MessageTemplate mt1;
            while (true)
            {
                ACLMessage reply = myAgent.receive(mt);
                if (reply!=null)
                {

                    ACLMessage cfpNav = new ACLMessage(ACLMessage.CFP);
                    try {
                        DFAgentDescription[] result;
                        while (true)
                        {
                            result = DFService.search(myAgent, templateNav);
                            if (result.length!=0)
                                break;
                        }
                        cfpNav.addReceiver(result[0].getName());

                    } catch (FIPAException e) {
                        e.printStackTrace();
                    }
                    cfpNav.setContent(reply.getContent().toString());
                    cfpNav.setConversationId("nav");

                    cfpNav.setReplyWith("cfpNav"+System.currentTimeMillis());
                    myAgent.send(cfpNav);
                    mt1 = MessageTemplate.and(MessageTemplate.MatchConversationId("nav"),
                            MessageTemplate.MatchInReplyTo(cfpNav.getReplyWith()));
                    break;
                }
            }
            while (true)
            {
                ACLMessage reply = myAgent.receive(mt1);
                if (reply!=null)
                {
                    System.out.println("Action" + reply.getContent());
                    message = reply.getContent();
                    break;
                }
            }
            myAgent.doWait(2000);

        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
