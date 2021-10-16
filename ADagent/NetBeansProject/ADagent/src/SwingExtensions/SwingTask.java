/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingExtensions;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;



/**
 * A type of {@link SwingWorker} that represents an application
 * background task.  Tasks add descriptive properties that can
 * be shown to the user, a new set of methods for customizing
 * task completion, support for blocking input to the GUI while
 * the Task is executing, and a {@code TaskListener} that
 * enables one to monitor the three key SwingWorker methods: {@code
 * doInBackground}, {@code process} and {@code done}.
 *
 * <p>
 * When a Task completes, the {@code final done} method invokes
 * one of {@code succeeded}, {@code cancelled}, {@code interrupted},
 * or {@code failed}.  The {@code final done} method invokes
 * {@code finished} when the completion method returns or throws
 * an exception.
 *
 * <p>
 * Tasks should provide localized values for the {@code title},
 * {@code description}, and {@code message} properties in a
 * ResourceBundle for the Task subclass.  A
 * {@link ResourceMap} is
 * loaded automatically using the Task subclass as the
 * {@code startClass} and Task.class the {@code stopClass}.
 * This ResourceMap is also used to look up format strings used in
 * calls to {@link #message message}, which is used to set
 * the {@code message} property.
 *
 * <p>
 * For example: given a Task called {@code MyTask} defined like this:
 * <pre>
 * class MyTask extends Task<MyResultType, Void> {
 *     protected MyResultType doInBackground() {
 *         message("startMessage", getPlannedSubtaskCount());
 *         // do the work ... if an error is encountered:
 *             message("errorMessage");
 *         message("finishedMessage", getActualSubtaskCount(), getFailureCount());
 *         // .. return the result
 *     }
 * }
 * </pre>
 * Typically the resources for this class would be defined in the
 * MyTask ResourceBundle, @{code resources/MyTask.properties}:
 * <pre>
 * title = My Task
 * description = A task of mine for my own purposes.
 * startMessage = Starting: working on %s subtasks...
 * errorMessage = An unexpected error occurred, skipping subtask
 * finishedMessage = Finished: completed %1$s subtasks, %2$s failures
 * </pre>
 *
 * <p>
 * Task subclasses can override resource values in their own ResourceBundles:
 * <pre>
 * class MyTaskSubclass extends MyTask {
 * }
 * # resources/MyTaskSubclass.properties
 * title = My Task Subclass
 * description = An appropriate description
 * # ... all other resources are inherited
 * </pre>
 *
 * <p>
 * Tasks can specify that input to the GUI is to be blocked while
 * they're being executed.  The {@code inputBlocker} property specifies
 * what part of the GUI is to be blocked and how that's accomplished.
 * The  {@code inputBlocker} is set automatically when an {@code @Action}
 * method that returns a Task specifies a {@link BlockingScope} value
 * for the {@code block} annotation parameter.  To customize the way
 * blocking is implemented you can define your own {@code Task.InputBlocker}.
 * For example, assume that {@code busyGlassPane} is a component
 * that consumes (and ignores) keyboard and mouse input:
 * <pre>
 * class MyInputBlocker extends InputBlocker {
 *     BusyIndicatorInputBlocker(Task task) {
 *         super(task, Task.BlockingScope.WINDOW, myGlassPane);
 *     }
 *     protected void block() {
 *         myFrame.setGlassPane(myGlassPane);
 *         busyGlassPane.setVisible(true);
 *     }
 *     protected void unblock() {
 *       busyGlassPane.setVisible(false);
 *     }
 * }
 * // ...
 * myTask.setInputBlocker(new MyInputBlocker(myTask));
 * </pre>
 * <p>
 * All of the settable properties in this class are bound, i.e. a
 * PropertyChangeEvent is fired when the value of the property changes.
 * As with the {@code SwingWorker} superclass, all
 * {@code PropertyChangeListeners} run on the event dispatching thread.
 * This is also true of {@code TaskListeners}.
 *
 * <p>
 * Unless specified otherwise specified, this class is thread-safe.
 * All of the Task properties can be get/set on any thread.
 * @param <T> the result type returned by this {@code SwingWorker's}
 *        {@code doInBackground} and {@code get} methods
 * @param <V> the type used for carrying out intermediate results by this
 *        {@code SwingWorker's} {@code publish} and {@code process} methods
 *
 * @author Hans Muller (Hans.Muller@Sun.COM)
 * @author Peransin Nicolas
 * @see ApplicationContext
 * @see ResourceMap
 * @see TaskListener
 * @see TaskEvent
 */
public abstract class SwingTask<T, V> extends SwingWorker<T, V> {

    public static final String STATE_PROP = "state"; // As defined in SwingWorker
    public static final String PROGRESS_PROP = "progress"; // As defined in SwingWork
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_INPUTBLOCKER = "inputBlocker";
    public static final String MESSAGE_PROP = "message";
    public static final String PROP_TASKSERVICE = "taskService";
    public static final String PROP_TITLE = "title";
    public static final String USERCANCELLABLE_PROP = "userCancellable";
    public static final String PROP_COMPLETED = "completed";
    public static final String PROP_DONE = "done";
    public static final String PROP_STARTED = "started";


    private Locale locale = JComponent.getDefaultLocale();
    private InputBlocker inputBlocker;
    private String title = null;
    private String description = null;
    private long messageTime = -1L;
    private String message = null;
    private long startTime = -1L;
    private long doneTime = -1L;
    private boolean userCancellable = true;
    private boolean progressPropertyIsValid = false;


    /**
     * Specifies to what extent the GUI should be blocked a Task
     * is executed by a TaskService.  Input blocking is carried
     * out by the Task's {@link #getInputBlocker inputBlocker}.
     *
     * @see Task.InputBlocker
     * @see Action#block
     */
    public enum BlockingScope {

        /**
         * Don't block the GUI while this Task is executing.
         */
        NONE,

        /**
         * Block an {@link ApplicationAction Action} while the
         * task is executing, typically by temporarily disabling it.
         */
        ACTION,

        /**
         * Block a component while the
         * task is executing, typically by temporarily disabling it.
         */
        COMPONENT,

        /**
         * Block a top level window while the task is executing,
         * typically by showing a window-modal dialog.
         */
        WINDOW,

        /**
         * Block all of the application's top level windows,
         * typically by showing a application-modal dialog.
         */
        APPLICATION
    }

    /**
     * Construct a {@code Task} with an empty (<code>""</code>) resource name
     * prefix, whose ResourceMap is the value of
     * <code>ApplicationContext.getInstance().getResourceMap(this.getClass(),
     * Task.class)</code>.
     * @param app
     */
    public SwingTask() {
        addPropertyChangeListener(new StatePCL());
        taskListeners = new CopyOnWriteArrayList<TaskListener<T, V>>();
    }


   

    /**
     * Return the value of the {@code title} property.
     * The default value of this property is the value of the
     * {@link #getResourceMap resourceMap's} {@code title} resource.
     * <p>
     * Returns a brief one-line description of the this Task that would
     * be useful for describing this task to the user.  The default
     * value of this property is null.
     *
     * @return the value of the {@code title} property.
     * @see #setTitle
     * @see #setDescription
     * @see #setMessage
     */
    public synchronized String getTitle() {
        return title;
    }

    /**
     * Set the {@code title} property.
     * The default value of this property is the value of the
     * {@link #getResourceMap resourceMap's} {@code title} resource.
     * <p>
     * The title is a brief one-line description of the this Task that
     * would be useful for describing it to the user.  The {@code
     * title} should be specific to this Task, for example "Loading
     * image sunset.png" is better than "Image Loader".  Similarly the
     * title isn't intended for ephemeral messages, like "Loaded 27.3%
     * of sunset.png".  The {@link #setMessage message} property is
     * for reporting the Task's current status.
     *
     * @param title a brief one-line description of the this Task.
     * @see #getTitle
     * @see #setDescription
     * @see #setMessage
     */
    public void setTitle(String title) { // Public to be injected
        String oldTitle, newTitle;
        synchronized (this) {
            oldTitle = this.title;
            this.title = title;
            newTitle = this.title;
        }
        firePropertyChange(PROP_TITLE, oldTitle, newTitle);
    }

    /**
     * Return the value of the {@code description} property.
     * The default value of this property is the value of the
     * {@link #getResourceMap resourceMap's} {@code description} resource.
     * <p>
     * A longer version of the Task's title; a few sentences that
     * describe what the Task is for in terms that an application
     * user would understand.
     *
     * @return the value of the {@code description} property.
     * @see #setDescription
     * @see #setTitle
     * @see #setMessage
     */
    public synchronized String getDescription() {
        return description;
    }

    /**
     * Set the {@code description} property.
     * The default value of this property is the value of the
     * {@link #getResourceMap resourceMap's} {@code description} resource.
     * <p>
     * The description is a longer version of the Task's title.
     * It should be a few sentences that describe what the Task is for,
     * in terms that an application user would understand.
     *
     * @param description a few sentences that describe what this Task is for.
     * @see #getDescription
     * @see #setTitle
     * @see #setMessage
     */
    public void setDescription(String description) {
        String oldDescription, newDescription;
        synchronized (this) {
            oldDescription = this.description;
            this.description = description;
            newDescription = this.description;
        }
        firePropertyChange(PROP_DESCRIPTION, oldDescription, newDescription);
    }

    /**
     * Returns the length of time this Task has run.  If the task hasn't
     * started yet (i.e. if its state is still {@code StateValue.PENDING}),
     * then this method returns 0.  Otherwise it returns the duration in the
     * specified time units.  For example, to learn how many seconds a
     * Task has run so far:
     * <pre>
     * long nSeconds = myTask.getExecutionDuration(TimeUnit.SECONDS);
     * </pre>
     *
     * @param unit the time unit of the return value
     * @return the length of time this Task has run.
     * @see #execute
     */
    public long getExecutionDuration(TimeUnit unit) {
        long startTime, doneTime, dt;
        synchronized (this) {
            startTime = this.startTime;
            doneTime = this.doneTime;
        }
        if (startTime == -1L) {
            dt = 0L;
        } else if (doneTime == -1L) {
            dt = System.currentTimeMillis() - startTime;
        } else {
            dt = doneTime - startTime;
        }
        return unit.convert(Math.max(0L, dt), TimeUnit.MILLISECONDS);
    }

    /**
     * Return the value of the {@code message} property.
     * The default value of this property is the value of the
     * {@link #getResourceMap resourceMap's} {@code message} resource.
     * <p>
     * Returns a short, one-line, message that explains what the task
     * is up to in terms appropriate for an application user.
     *
     * @return a short one-line status message.
     * @see #setMessage
     * @see #getMessageDuration
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the {@code message} property.
     * The default value of this property is the value of the
     * {@link #getResourceMap resourceMap's} {@code message} resource.
     * <p>
     * Returns a short, one-line, message that explains what the task is
     * up to in terms appropriate for an application user.  This message
     * should reflect that Task's dynamic state and can be reset as
     * frequently one could reasonably expect a user to understand.
     * It should not repeat the information in the Task's title and
     * should not convey any information that the user shouldn't ignore.
     * <p>
     * For example, a Task whose {@code doInBackground} method loaded
     * a photo from a web service might set this property to a new
     * value each time a new internal milestone was reached, e.g.:
     * <pre>
     * loadTask.setTitle("Loading photo from http://photos.com/sunset");
     * // ...
     * loadTask.setMessage("opening connection to photos.com");
     * // ...
     * loadTask.setMessage("reading thumbnail image file sunset.png");
     * // ... etc
     * </pre>
     * <p>
     * Each time this property is set, the {@link #getMessageDuration
     * messageDuration} property is reset.  Since status messages are
     * intended to be ephemeral, application GUI elements like status
     * bars may want to clear messages after 20 or 30 seconds have
     * elapsed.
     * <p>
     * Localized messages that require paramters can be constructed
     * with the {@link #message message} method.
     *
     * @param message a short one-line status message.
     * @see #getMessage
     * @see #getMessageDuration
     * @see #message
     */
    public void setMessage(String message) {
        String oldMessage, newMessage;
        synchronized (this) {
            oldMessage = this.message;
            this.message = message;
            newMessage = this.message;
            messageTime = System.currentTimeMillis();
        }
        firePropertyChange(MESSAGE_PROP, oldMessage, newMessage);
    }

    /**
     * Set the message property to a string generated with {@code
     * String.format} and the specified arguments.  The {@code
     * messageKey} names a resource whose value is a format
     * string.  See the Task class javadoc for an example.
     * <p>
     * Note that if the no arguments are specified, this method is
     * comparable to:
     *<pre>
     * setMessage(getResourceMap().getString(resourceName(messageKey)));
     *</pre>
     * <p>
     * If a {@code ResourceMap} was not specified for this Task,
     * then set the {@code message} property to {@code formatResourceKey}.
     *
     * @param messageKey the suffix of the format string's resource name.
     * @param args the arguments referred to by the placeholders in the format string
     * @see #setMessage
     * @see ResourceMap#getString(String, Object...)
     * @see java.text.MessageFormat
     */
    protected void message(Object messageKey, Object... args) {
        if (messages != null) {
            setMessage(messages.get(messageKey, args));
        } else {
            setMessage(String.valueOf(messageKey));
        }
    }

    /**
     * Returns the length of time that has elapsed since the {@code
     * message} property was last set.
     *
     * @param unit units for the return value
     * @return elapsed time since the {@code message} property was last set.
     * @see #setMessage
     */
    public long getMessageDuration(TimeUnit unit) {
        long messageTime;
        synchronized (this) {
            messageTime = this.messageTime;
        }
        long dt = (messageTime == -1L) ? 0L : Math.max(0L, System.currentTimeMillis() - messageTime);
        return unit.convert(dt, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the value of the {@code userCanCancel} property.
     * The default value of this property is true.
     * <p>
     * Generic GUI components, like a Task progress dialog, can use this
     * property to decide if they should provide a way for the
     * user to cancel this task.
     *
     * @return true if the user can cancel this Task.
     * @see #setUserCanCancel
     */
    public synchronized boolean getUserCancellable() {
        return userCancellable;
    }

    /**
     * Sets the {@code userCanCancel} property.
     * The default value of this property is true.
     * <p>
     * Generic GUI components, like a Task progress dialog, can use this
     * property to decide if they should provide a way for the
     * user to cancel this task. For example, the value of this
     * property might be bound to the enabled property of a
     * cancel button.
     * <p>
     * This property has no effect on the {@link #cancel} cancel
     * method.  It's just advice for GUI components that display
     * this Task.
     *
     * @param userCanCancel true if the user should be allowed to cancel this Task.
     * @see #getUserCanCancel
     */
    protected void setUserCancellable(boolean userCanCancel) {
        boolean oldValue, newValue;
        synchronized (this) {
            oldValue = this.userCancellable;
            this.userCancellable = userCanCancel;
            newValue = this.userCancellable;
        }
        firePropertyChange(USERCANCELLABLE_PROP, oldValue, newValue);
    }

    /**
     * Returns true if the {@link #setProgress progress} property has
     * been set.  Some Tasks don't update the progress property
     * because it's difficult or impossible to determine how what
     * percentage of the task has been completed.  GUI elements that
     * display Task progress, like an application status bar, can use this
     * property to set the @{link JProgressBar#indeterminate
     * indeterminate} @{code JProgressBar} property.
     * <p>
     * A task that does keep the progress property up to
     * date should initialize it to 0, to ensure that {@code
     * isProgressPropertyValid} is always true.
     *
     * @return true if the {@link #setProgress progress} property has been set.
     * @see #setProgress
     */
    public synchronized boolean isProgressPropertyValid() {
        return progressPropertyIsValid;
    }

    /**
     * A convenience method that sets the {@code progress} property to the following
     * ratio normalized to 0 .. 100.
     * <pre>
     * value - min / max - min
     * </pre>
     *
     * @param value a value in the range min ... max, inclusive
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @see #setProgress(int)
     */
    protected final void setProgress(int value, int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("invalid range: min >= max");
        }
        if ((value < min) || (value > max)) {
            throw new IllegalArgumentException("invalid value");
        }
        float percentage = (float) (value - min) / (float) (max - min);
        setProgress(Math.round(percentage * 100.0f));
    }

    /**
     * A convenience method that sets the {@code progress} property to
     * <code>percentage * 100</code>.
     *
     * @param percentage a value in the range 0.0 ... 1.0 inclusive
     * @see #setProgress(int)
     */
    protected final void setProgress(float percentage) {
        if ((percentage < 0.0) || (percentage > 1.0)) {
            throw new IllegalArgumentException("invalid percentage");
        }
        setProgress(Math.round(percentage * 100.0f));
    }

    /**
     * A convenience method that sets the {@code progress} property to the following
     * ratio normalized to 0 .. 100.
     * <pre>
     * value - min / max - min
     * </pre>
     *
     * @param value a value in the range min ... max, inclusive
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @see #setProgress(int)
     */
    protected final void setProgress(float value, float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("invalid range: min >= max");
        }
        if ((value < min) || (value > max)) {
            throw new IllegalArgumentException("invalid value");
        }
        float percentage = (value - min) / (max - min);
        setProgress(Math.round(percentage * 100.0f));
    }

    /**
     * Equivalent to {@code getState() == StateValue.PENDING}.
     * <p>
     * When a pending Task's state changes to {@code StateValue.STARTED}
     * a PropertyChangeEvent for the "started" property is fired.  Similarly
     * when a started Task's state changes to {@code StateValue.DONE}, a
     * "done" PropertyChangeEvent is fired.
     */
    public final boolean isPending() {
        return getState() == StateValue.PENDING;
    }

    /**
     * Equivalent to {@code getState() == StateValue.STARTED}.
     * <p>
     * When a pending Task's state changes to {@code StateValue.STARTED}
     * a PropertyChangeEvent for the "started" property is fired.  Similarly
     * when a started Task's state changes to {@code StateValue.DONE}, a
     * "done" PropertyChangeEvent is fired.
     * @return true if the state is {@code STARTED}
     */
    public final boolean isStarted() {
        return getState() == StateValue.STARTED;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method fires the TaskListeners' {@link TaskListener#process process}
     * method.  If you override {@code process} and do not call
     * {@code super.process(values)}, then the TaskListeners will not run.
     *
     * @param values @{inheritDoc}
     */
    @Override
    protected void process(List<V> values) {
        fireProcessListeners(values);
    }

    @Override
    protected final void done() {
        setTaskService(null);
    }

    /**
     * Called when this Task has been cancelled by {@link #cancel(boolean)}.
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @see #done
     */
    protected void cancelled() {
    }

    /**
     * Called when this Task has successfully completed, i.e. when
     * its {@code get} method returns a value.  Tasks that compute
     * a value should override this method.
     * <p>
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @param result the value returned by the {@code get} method
     * @see #done
     * @see #get
     * @see #failed
     */
    protected void succeeded(T result) {
    }

    /**
     * Called if the Task's Thread is interrupted but not
     * explicitly cancelled.
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @param e the {@code InterruptedException} thrown by {@code get}
     * @see #cancel
     * @see #done
     * @see #get
     */
    protected void interrupted(InterruptedException e) {
    }

    /**
     * Called when an execution of this Task fails and an
     * {@code ExecutionExecption} is thrown by {@code get}.
     * <p>
     * This method runs on the EDT.  It Logs an error message by default.
     *
     * @param cause the {@link Throwable#getCause cause} of the {@code ExecutionException}
     * @see #done
     * @see #get
     * @see #failed
     */
    protected void failed(Throwable cause) {
        TaskService service = getTaskService();
        if (service != null) {
            service.failed(this, cause);
        } else {
            System.err.println("Failure during " + this + " execution");
            if (cause != null) {
                cause.printStackTrace(System.err);
            }
        }
    }

    /**
     * Called unconditionally (in a {@code finally} clause) after one
     * of the completion methods, {@code succeeded}, {@code failed},
     * {@code cancelled}, or {@code interrupted}, runs.  Subclasses
     * can override this method to cleanup before the {@code done}
     * method returns.
     * <p>
     * This method runs on the EDT.  It does nothing by default.
     *
     * @see #done
     * @see #get
     * @see #failed
     */
    protected void finished() {
    }

    /**
     * Adds a {@code TaskListener} to this Task. The listener will
     * be notified when the Task's state changes to {@code STARTED},
     * each time the {@code process} method is called, and when the
     * Task's state changes to {@code DONE}.  All of the listener
     * methods will run on the event dispatching thread.
     *
     * @param listener the {@code TaskListener} to be added
     * @see #removeTaskListener
     */
    public void addTaskListener(TaskListener<T, V> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("null listener");
        }
        taskListeners.add(listener);
    }

    /**
     * Removes a {@code TaskListener} from this Task.  If the specified
     * listener doesn't exist, this method does nothing.
     *
     * @param listener the {@code TaskListener} to be added
     * @see #addTaskListener
     */
    public void removeTaskListener(TaskListener<T, V> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("null listener");
        }
        taskListeners.remove(listener);
    }

    /**
     * Returns a copy of this Task's {@code TaskListeners}.
     *
     * @return a copy of this Task's {@code TaskListeners}.
     * @see #addTaskListener
     * @see #removeTaskListener
     */
    @SuppressWarnings("unchecked")
    public TaskListener<T, V>[] getTaskListeners() {
        return taskListeners.toArray(new TaskListener[taskListeners.size()]);
    }

    /* This method is guaranteed to run on the EDT, it's called
     * from SwingWorker.process().
     */
    private void fireProcessListeners(List<V> values) {
        TaskEvent<List<V>> event = new TaskEvent<List<V>>(this, values);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.process(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireDoInBackgroundListeners() {
        TaskEvent<Void> event = new TaskEvent<Void>(this, null);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.doInBackground(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireSucceededListeners(T result) {
        TaskEvent<T> event = new TaskEvent<T>(this, result);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.succeeded(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireCancelledListeners() {
        TaskEvent<Void> event = new TaskEvent<Void>(this, null);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.cancelled(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireInterruptedListeners(InterruptedException e) {
        TaskEvent<InterruptedException> event = new TaskEvent<InterruptedException>(this, e);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.interrupted(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireFailedListeners(Throwable e) {
        TaskEvent<Throwable> event = new TaskEvent<Throwable>(this, e);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.failed(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireFinishedListeners() {
        TaskEvent<Void> event = new TaskEvent<Void>(this, null);
        for (TaskListener<T, V> listener : taskListeners) {
            listener.finished(event);
        }
    }

    /* This method runs on the EDT because it's called from
     * StatePCL (see below).
     */
    private void fireCompletionListeners() {
        try {
            if (isCancelled()) {
                fireCancelledListeners();
            } else {
                try {
                    fireSucceededListeners(get());
                } catch (InterruptedException e) {
                    fireInterruptedListeners(e);
                } catch (ExecutionException e) {
                    fireFailedListeners(e.getCause());
                }
            }
        } finally {
            fireFinishedListeners();
        }
    }

    private class StatePCL implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (STATE_PROP.equals(propertyName)) {
                StateValue state = (StateValue) (e.getNewValue());
                switch (state) {
                    case STARTED:
                        taskStarted();
                        break;
                    case DONE:
                        taskDone();
                        break;
                }
            } else if (PROGRESS_PROP.equals(propertyName)) {
                synchronized (Task.this) {
                    progressPropertyIsValid = true;
                }
            }
        }

        private void taskStarted() {
            synchronized (Task.this) {
                startTime = System.currentTimeMillis();
            }
            firePropertyChange(PROP_STARTED, false, true);
            fireDoInBackgroundListeners();
        }

        private void taskDone() {
            synchronized (Task.this) {
                doneTime = System.currentTimeMillis();
            }
            try {
                removePropertyChangeListener(this);
                firePropertyChange(PROP_DONE, false, true);
            } finally {
                // execute succeeded only when SwingWorker is done.
                SwingUtilities.invokeLater(taskDoneExecution);
            }
        }
    }

    private final Runnable taskDoneExecution = new Runnable() {

        @Override
        public void run() {
            try {
                if (isCancelled()) {
                    cancelled();
                } else {
                    try {
                        succeeded(get());
                    } catch (InterruptedException e) {
                        interrupted(e);
                    } catch (ExecutionException e) {
                        failed(e.getCause());
                    }
                }
            } finally {
                finished();
                try {
                    fireCompletionListeners();
                } finally {
                    firePropertyChange(PROP_COMPLETED, false, true);
                }
            }
        }
    };

    /**
     * Return this task's InputBlocker.
     * <p>
     * This is a bound property.
     *
     * @return this task's InputBlocker
     * @see #setInputBlocker
     */
    public final InputBlocker getInputBlocker() {
        return inputBlocker;
    }

    /**
     * Set this task's InputBlocker.  The InputBlocker defines
     * to what extent the GUI should be blocked while the Task
     * is executed by a TaskService.  It is not used by the Task
     * directly, it's used by the TaskService that executes the Task.
     * <p>
     * This property may only be set before the Task is
     * {@link TaskService#execute submitted} to a TaskService for
     * execution.  If it's called afterwards, an IllegalStateException
     * is thrown.
     * <p>
     * This is a bound property.
     *
     * @param inputBlocker
     * @see #getInputBlocker
     */
    public final void setInputBlocker(InputBlocker inputBlocker) {
        if (getTaskService() != null) {
            throw new IllegalStateException("task already being executed");
        }
        InputBlocker oldInputBlocker, newInputBlocker;
        synchronized (this) {
            oldInputBlocker = this.inputBlocker;
            this.inputBlocker = inputBlocker;
            newInputBlocker = this.inputBlocker;
        }
        firePropertyChange(PROP_INPUTBLOCKER, oldInputBlocker, newInputBlocker);
    }

    /**
     * Specifies to what extent input to the Application's GUI should
     * be blocked while this Task is being executed and provides
     * a pair of methods, {@code block} and {@code unblock} that
     * do the work of blocking the GUI.  For the sake of input blocking,
     * a Task begins executing when it's {@link TaskService#execute submitted}
     * to a {@code TaskService}, and it finishes executing after
     * the Task's completion methods have been called.
     * <p>
     * The InputBlocker's {@link Task.BlockingScope
     * BlockingScope} and the blocking {@code target} object define
     * what part of the GUI's input will be blocked:
     * <dl>
     * <dt><b><code>Task.BlockingScope.NONE</code></b><dt>Don't block input.  The blocking
     * target is ignored in this case.<p></p>
     * <dt><b><code>Task.BlockingScope.ACTION</code></b><dt>Disable the target
     * {@link javax.swing.Action Action} while the Task is executing.<p></p>
     * <dt><b><code>Task.BlockingScope.COMPONENT</code></b><dt>Disable the target
     * {@link java.awt.Component} Component while the Task is executing. <p></p>
     * <dt><b><code>Task.BlockingScope.WINDOW</code></b><dt> Block the Window ancestor of the
     * target Component while the Task is executing.<p></p>
     * <dt><b><code>Task.BlockingScope.Application</code></b><dt> Block the entire Application
     * while the Task is executing.  The blocking target is ignored
     * in this case.<p></p>
     * </dl>
     * <p>
     * Input blocking begins when the {@code block} method is called and
     * ends when {@code unblock} is called.  Each method is only
     * called once, typically by the {@code TaskService}.
     *
     * @see Task#getInputBlocker
     * @see Task#setInputBlocker
     * @see TaskService
     * @see Action
     */
    public static abstract class InputBlocker extends SwingBean {

        private final Application application;
        private final Task<?, ?> task;
        private final BlockingScope scope;
        private final Object target;

        /**
         * Construct an InputBlocker with four immutable properties.  If
         * the Task is null or if the Task has already been
         * executed by a TaskService, then an exception is thrown.
         * If scope is {@code BlockingScope.ACTION} then target must be
         * a {@link javax.swing.Action Action}.  If scope is
         * {@code BlockingScope.WINDOW} or {@code BlockingScope.COMPONENT}
         * then target must be a Component.
         *
         * @param task block input while this Task is executing
         * @param scope how much of the GUI will be blocked
         * @param target the GUI element that will be blocked
         * @param action the {@code @Action} that triggered running the task, or null
         * @see TaskService#execute
         */
        public InputBlocker(Task<?, ?> task, BlockingScope scope, Object target) {
            this(task, scope, target, null);
        }

        public InputBlocker(Task<?, ?> task, BlockingScope scope, Object target, Application app) {
            if (task == null) {
                throw new IllegalArgumentException("null task");
            }
            if (task.getTaskService() != null) {
                throw new IllegalStateException("task already being executed");
            }
            switch (scope) {
                case ACTION:
                    if (!(target instanceof javax.swing.Action)) {
                        throw new IllegalArgumentException("target not an Action");
                    }
                    break;
                case COMPONENT:
                case WINDOW:
                    if (!(target instanceof Component)) {
                        throw new IllegalArgumentException("target not a Component");
                    }
                    break;
            }
            this.task = task;
            this.scope = scope;
            this.target = target;
            this.application = app;
        }


        /**
         * Returns the application.
         *
         * @return the application
         */
        public Application getApplication() {
            return application;
        }
        
        /**
         * Returns the application.
         *
         * @return the application
         */
        public ApplicationContext getApplicationContext() {
            return application.getContext();
        }

        /**
         * The {@code block} method will block input while this Task
         * is being executed by a TaskService.
         *
         * @return the value of the read-only Task property
         * @see #block
         * @see #unblock
         */
        public final Task<?, ?> getTask() {
            return task;
        }

        /**
         * Defines the extent to which the GUI is blocked while
         * the task is being executed.
         *
         * @return the value of the read-only blockingScope  property
         * @see #block
         * @see #unblock
         */
        public final BlockingScope getScope() {
            return scope;
        }

        /**
         * Specifies the GUI element that will be blocked while
         * the task is being executed.
         * <p>
         * This property may be null.
         *
         * @return the value of the read-only target  property
         * @see #getScope
         * @see #block
         * @see #unblock
         */
        public final Object getTarget() {
            return target;
        }


        /**
         * Block input to the GUI per the {@code scope} and {@code target}
         * properties.  This method will only be called once.
         *
         * @see #unblock
         * @see TaskService#execute
         */
        protected abstract void block();

        /**
         * Unblock input to the GUI by undoing whatever the {@code block}
         * method did.  This method will only be called once.
         *
         * @see #block
         * @see TaskService#execute
         */
        protected abstract void unblock();
    }


    
    /**
     * Returns the locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Sets the locale.
     *
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        SwingHelper.assertNotNull(Locales.LOCALE_PROP, locale);
        Locale old = this.locale;
        this.locale = locale;
        firePropertyChange(Locales.LOCALE_PROP, old, locale);
    }
    
}