/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import com.google.common.collect.Sets;

import java.util.Arrays;

final class SDDiffTestUtils {
  
  private static final SDObject ui = SDObject.builder().name("ui").build();
  private static final SDObject controller = SDObject.builder().name("controller").build();
  private static final SDObject planner = SDObject.builder().name("planner").build();
  private static final SDObject stateProvider = SDObject.builder().name("stateProvider").build();
  private static final SDObject actionExecutor = SDObject.builder().name("actionExecutor").build();

  private static final SDAction deliver = SDActionString.builder().action("deliver(r4222,wd40)").build();
  private static final SDAction getDeliverPlan = SDActionString.builder().action("getDeliverPlan(r4222,wd40)").build();
  private static final SDAction getState = SDActionString.builder().action("getState()").build();
  private static final SDAction state = SDActionString.builder().action("state").build();
  private static final SDAction plan = SDActionString.builder().action("plan").build();
  private static final SDAction abortAction = SDActionString.builder().action("abortAction()").build();
  private static final SDAction moveTo = SDActionString.builder().action("moveTo(r4222)").build();
  private static final SDAction ACTION_SUCCEEDED = SDActionString.builder().action("ACTION_SUCCEEDED").build();
  private static final SDAction ACTION_FAILED = SDActionString.builder().action("ACTION_FAILED").build();
  private static final SDAction ACTION_ABORTED = SDActionString.builder().action("ACTION_ABORTED").build();
  
  static SequenceDiagram rob1() {
    return SequenceDiagram.builder()
                          .objects(Sets.newHashSet(ui, controller, planner, stateProvider, actionExecutor))
                          .interactions(Arrays.asList(
                            new SDInteraction(ui, deliver, controller),
                            new SDInteraction(controller, getDeliverPlan, planner),
                            new SDInteraction(planner, getState, stateProvider),
                            new SDInteraction(stateProvider, state, planner),
                            new SDInteraction(planner, plan, controller),
                            new SDInteraction(controller, moveTo, actionExecutor),
                            new SDInteraction(actionExecutor, ACTION_SUCCEEDED, controller)
                          ))
                          .build();
  }

  static SequenceDiagram rob2() {
    return SequenceDiagram.builder()
                          .objects(Sets.newHashSet(ui, controller, planner, actionExecutor))
                          .interactions(Arrays.asList(
                            new SDInteraction(ui, deliver, controller),
                            new SDInteraction(controller, getDeliverPlan, planner),
                            new SDInteraction(planner, plan, controller),
                            new SDInteraction(controller, moveTo, actionExecutor),
                            new SDInteraction(actionExecutor, ACTION_SUCCEEDED, controller)
                          ))
                          .build();
  }

  static SequenceDiagram rob3() {
    return SequenceDiagram.builder()
                          .objects(Sets.newHashSet(ui, controller, planner, actionExecutor))
                          .completeObjects(Sets.newHashSet(controller))
                          .interactions(Arrays.asList(
                            new SDInteraction(ui, deliver, controller),
                            new SDInteraction(controller, getDeliverPlan, planner),
                            new SDInteraction(planner, plan, controller),
                            new SDInteraction(controller, moveTo, actionExecutor),
                            new SDInteraction(actionExecutor, ACTION_SUCCEEDED, controller)
                          ))
                          .build();
  }

  static SequenceDiagram rob4() {
    return SequenceDiagram.builder()
                          .objects(Sets.newHashSet(ui, controller, planner, stateProvider, actionExecutor))
                          .interactions(Arrays.asList(
                            new SDInteraction(ui, deliver, controller),
                            new SDInteraction(controller, getDeliverPlan, planner),
                            new SDInteraction(planner, getState, stateProvider),
                            new SDInteraction(stateProvider, state, planner),
                            new SDInteraction(planner, plan, controller),
                            new SDInteraction(controller, moveTo, actionExecutor),
                            new SDInteraction(actionExecutor, ACTION_FAILED, controller)
                          ))
                          .build();
  }

  static SequenceDiagram rob5() {
    return SequenceDiagram.builder()
                          .objects(Sets.newHashSet(ui, controller, planner, stateProvider, actionExecutor))
                          .interactions(Arrays.asList(
                            new SDInteraction(ui, deliver, controller),
                            new SDInteraction(controller, getDeliverPlan, planner),
                            new SDInteraction(planner, getState, stateProvider),
                            new SDInteraction(stateProvider, state, planner),
                            new SDInteraction(planner, plan, controller),
                            new SDInteraction(ui, abortAction, actionExecutor),
                            new SDInteraction(controller, moveTo, actionExecutor),
                            new SDInteraction(actionExecutor, ACTION_ABORTED, controller)
                          ))
                          .build();
  }

}
