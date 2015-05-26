/*
 *      Copyright (C) 2012-2015 DataStax Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.datastax.driver.core.exceptions;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.WriteType;

/**
 * A non-timeout error during a write query.
 * <p>
 * This happens when some of the replicas that were contacted by the coordinator replied with an error.
 */
@SuppressWarnings("serial")
public class WriteFailureException extends QueryConsistencyException {
    private final WriteType writeType;
    private final int failed;

    public WriteFailureException(ConsistencyLevel consistency, WriteType writeType, int received, int required, int failed) {
        super(String.format("Cassandra failure during write query at consistency %s "
                    + "(%d responses were required but only %d replica responded, %d failed)",
                consistency, required, received, failed),
            consistency,
            received,
            required);
        this.writeType = writeType;
        this.failed = failed;
    }

    private WriteFailureException(String msg, Throwable cause,
                                  ConsistencyLevel consistency, WriteType writeType, int received, int required, int failed) {
        super(msg, cause, consistency, received, required);
        this.writeType = writeType;
        this.failed = failed;
    }

    /**
     * The type of the write for which a timeout was raised.
     *
     * @return the type of the write for which a timeout was raised.
     */
    public WriteType getWriteType() {
        return writeType;
    }

    /**
     * Returns the number of replicas that experienced a failure while executing the request.
     *
     * @return the number of failures.
     */
    public int getFailures() {
        return failed;
    }

    @Override
    public DriverException copy() {
        return new WriteFailureException(getMessage(), this, getConsistencyLevel(), getWriteType(),
            getReceivedAcknowledgements(), getRequiredAcknowledgements(), getFailures());
    }
}
