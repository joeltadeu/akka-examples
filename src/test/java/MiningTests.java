import akka.actor.testkit.typed.CapturedLogEvent;
import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import com.akka.examples.blockchain.behavior.BlockChainWorkerBehavior;
import com.akka.examples.blockchain.command.HashResultCommand;
import com.akka.examples.blockchain.command.ManagerCommand;
import com.akka.examples.blockchain.command.WorkerCommand;
import com.akka.examples.blockchain.model.Block;
import com.akka.examples.blockchain.model.HashResult;
import com.akka.examples.blockchain.utils.BlocksData;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MiningTests {

    @Test
    void testMiningFailsIfNonceNotInRange() {
        BehaviorTestKit<WorkerCommand> testActor = BehaviorTestKit.create(BlockChainWorkerBehavior.create());
        Block block = BlocksData.getNextBlock(0, "0");

        TestInbox<ManagerCommand> testInbox = TestInbox.create();

        WorkerCommand message = new WorkerCommand(block, 0, 5, testInbox.getRef());
        testActor.run(message);
        List<CapturedLogEvent> logMessages = testActor.getAllLogEntries();
        assertEquals(logMessages.size(), 1);
        assertEquals(logMessages.get(0).message() , "null" );
        assertEquals(logMessages.get(0).level() , Level.DEBUG );
    }

    @Test
    void testMiningPassesIfNonceIsInRange() {
        BehaviorTestKit<WorkerCommand> testActor = BehaviorTestKit.create(BlockChainWorkerBehavior.create());
        Block block = BlocksData.getNextBlock(0, "0");

        TestInbox<ManagerCommand> testInbox = TestInbox.create();

        WorkerCommand message = new WorkerCommand(block, 82700, 5, testInbox.getRef());
        testActor.run(message);
        List<CapturedLogEvent> logMessages = testActor.getAllLogEntries();
        assertEquals(logMessages.size(), 1);
        String expectedResult = "82741 : 0000081e9d118bf0827bed8f4a3e142a99a42ef29c8c3d3e24ae2592456c440b";
        assertEquals(logMessages.get(0).message() , expectedResult );
        assertEquals(logMessages.get(0).level() , Level.DEBUG );
    }

    @Test
    void testMessageReceivedIfNonceInRange() {
        BehaviorTestKit<WorkerCommand> testActor = BehaviorTestKit.create(BlockChainWorkerBehavior.create());
        Block block = BlocksData.getNextBlock(0, "0");

        TestInbox<ManagerCommand> testInbox = TestInbox.create();

        WorkerCommand message = new WorkerCommand(block, 82700, 5, testInbox.getRef());
        testActor.run(message);

        HashResult expectedHashResult = new HashResult();
        expectedHashResult.foundAHash("0000081e9d118bf0827bed8f4a3e142a99a42ef29c8c3d3e24ae2592456c440b", 82741);
        ManagerCommand expectedCommand = new HashResultCommand(expectedHashResult);
        testInbox.expectMessage(expectedCommand);
    }

    @Test
    void testNoMessageReceivedIfNonceNotInRange() {
        BehaviorTestKit<WorkerCommand> testActor = BehaviorTestKit.create(BlockChainWorkerBehavior.create());
        Block block = BlocksData.getNextBlock(0, "0");

        TestInbox<ManagerCommand> testInbox = TestInbox.create();

        WorkerCommand message = new WorkerCommand(block, 0, 5, testInbox.getRef());
        testActor.run(message);

        assertFalse(testInbox.hasMessages());
    }
}
