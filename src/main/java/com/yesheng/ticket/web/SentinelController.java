package com.yesheng.ticket.web;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SentinelController {

  @PostConstruct
  public void ticketsFlow() {
    List<FlowRule> rules = new ArrayList<>();
    // ticket activity
    FlowRule rule_activity = new FlowRule();
    rule_activity.setResource("ticket_activity");
    rule_activity.setGrade(RuleConstant.FLOW_GRADE_QPS);
    rule_activity.setCount(1);
    rules.add(rule_activity);

    FlowRuleManager.loadRules(rules);
  }
}
