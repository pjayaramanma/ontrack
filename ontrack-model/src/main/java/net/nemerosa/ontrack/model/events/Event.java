package net.nemerosa.ontrack.model.events;

import com.google.common.collect.Maps;
import lombok.Data;
import net.nemerosa.ontrack.model.structure.*;
import net.nemerosa.ontrack.model.support.NameValue;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.nemerosa.ontrack.model.events.PlainEventRenderer.INSTANCE;

/**
 * Definition of an event
 */
@Data
public final class Event {

    private static final Pattern EXPRESSION = Pattern.compile("\\$\\{([:a-zA-Z_]+)\\}");

    private final EventType eventType;
    private final Signature signature;
    private final Map<ProjectEntityType, ProjectEntity> entities;
    private final Map<String, NameValue> values;

    public String renderText() {
        return render(INSTANCE);
    }

    public String render(EventRenderer eventRenderer) {
        Matcher m = EXPRESSION.matcher(eventType.getTemplate());
        StringBuffer output = new StringBuffer();
        while (m.find()) {
            String value = expandExpression(m.group(1), eventRenderer);
            m.appendReplacement(output, value);
        }
        m.appendTail(output);
        return output.toString();
    }

    private String expandExpression(String expression, EventRenderer eventRenderer) {
        if (StringUtils.startsWith(expression, ":")) {
            String valueKey = expression.substring(1);
            NameValue value = values.get(valueKey);
            if (value == null) {
                throw new EventMissingValueException(eventType.getTemplate(), valueKey);
            }
            return eventRenderer.render(valueKey, value, this);
        } else {
            // Project entity type
            ProjectEntityType projectEntityType = ProjectEntityType.valueOf(expression);
            // Gets the corresponding entity
            ProjectEntity projectEntity = entities.get(projectEntityType);
            if (projectEntity == null) {
                throw new EventMissingEntityException(eventType.getTemplate(), projectEntityType);
            }
            // Rendering
            return eventRenderer.render(projectEntity, this);
        }
    }

    public static EventBuilder of(EventType eventType) {
        return new EventBuilder(eventType);
    }

    public Event withSignature(Signature signature) {
        return new Event(
                eventType,
                signature,
                entities,
                values
        );
    }

    public static class EventBuilder {

        private final EventType eventType;
        private Signature signature;
        private Collection<ProjectEntity> entities = new ArrayList<>();
        private Map<String, NameValue> values = new LinkedHashMap<>();

        public EventBuilder(EventType eventType) {
            this.eventType = eventType;
        }

        public EventBuilder with(Signature signature) {
            this.signature = signature;
            return this;
        }

        public EventBuilder withNoSignature() {
            this.signature = null;
            return this;
        }

        public EventBuilder withBuild(Build build) {
            return withBranch(build.getBranch()).with(build).with(build.getSignature());
        }

        public EventBuilder withPromotionRun(PromotionRun promotionRun) {
            return withBuild(promotionRun.getBuild()).with(promotionRun.getPromotionLevel()).with(promotionRun.getSignature());
        }

        public EventBuilder withValidationRun(ValidationRun validationRun) {
            return withBuild(validationRun.getBuild()).with(validationRun.getValidationStamp()).with(validationRun).with(validationRun.getLastStatus().getSignature());
        }

        public EventBuilder withPromotionLevel(PromotionLevel promotionLevel) {
            return withBranch(promotionLevel.getBranch()).with(promotionLevel);
        }

        public EventBuilder withValidationStamp(ValidationStamp validationStamp) {
            return withBranch(validationStamp.getBranch()).with(validationStamp);
        }

        public EventBuilder withBranch(Branch branch) {
            return withProject(branch.getProject()).with(branch);
        }

        public EventBuilder withProject(Project project) {
            return with(project);
        }

        public EventBuilder with(ProjectEntity entity) {
            entities.add(entity);
            return this;
        }

        public EventBuilder withValidationRunStatus(ValidationRunStatusID statusID) {
            return with("status", new NameValue(statusID.getId(), statusID.getName()));
        }

        public EventBuilder with(String name, NameValue value) {
            values.put(name, value);
            return this;
        }

        public EventBuilder with(String name, String value) {
            return with(name, new NameValue(name, value));
        }

        public Event get() {
            // Creates the event
            Event event = new Event(
                    eventType,
                    signature,
                    Maps.uniqueIndex(
                            entities,
                            ProjectEntity::getProjectEntityType
                    ),
                    values
            );
            // Checks the event can be resolved with all its references
            event.renderText();
            // OK
            return event;
        }
    }

}