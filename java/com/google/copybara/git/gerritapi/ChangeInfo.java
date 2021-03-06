/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.copybara.git.gerritapi;

import static com.google.copybara.git.gerritapi.GerritApiUtil.parseTimestamp;

import com.google.api.client.util.Key;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.devtools.build.lib.skylarkinterface.SkylarkCallable;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModule;
import com.google.devtools.build.lib.skylarkinterface.SkylarkModuleCategory;
import com.google.devtools.build.lib.skylarkinterface.SkylarkPrinter;
import com.google.devtools.build.lib.skylarkinterface.SkylarkValue;
import com.google.devtools.build.lib.syntax.SkylarkDict;
import com.google.devtools.build.lib.syntax.SkylarkList;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/** https://gerrit-review.googlesource.com/Documentation/rest-api-changes.html#change-info */
@SuppressWarnings("unused")
@SkylarkModule(
    name = "gerritapi.ChangeInfo",
    category = SkylarkModuleCategory.TOP_LEVEL_TYPE,
    doc = "Gerrit change information.",
    documented = false)
public class ChangeInfo implements SkylarkValue {

  @Key private String id;
  @Key private String project;
  @Key private String branch;
  @Key private String topic;
  @Key("change_id") private String changeId;
  @Key private String subject;
  @Key private String status;
  @Key private String created;
  @Key private String updated;
  @Key private String submitted;
  @Key("_number") private long number;
  @Key private AccountInfo owner;
  @Key private Map<String, LabelInfo> labels;
  @Key private List<ChangeMessageInfo> messages;
  @Key("current_revision") private String currentRevision;
  @Key("revisions") private Map<String, RevisionInfo> allRevisions;
  @Key("_more_changes") private boolean moreChanges;
  @Key private Map<String, List<AccountInfo>> reviewers;

  @SkylarkCallable(
      name = "id",
      doc =
          "The ID of the change in the format \"'<project>~<branch>~<Change-Id>'\", where "
              + "'project', 'branch' and 'Change-Id' are URL encoded. For 'branch' the "
              + "refs/heads/ prefix is omitted.",
      structField = true,
      allowReturnNones = true)
  public String getId() {
    return id;
  }

  @SkylarkCallable(
      name = "project",
      doc = "The name of the project.",
      structField = true,
      allowReturnNones = true)
  public String getProject() {
    return project;
  }

  @SkylarkCallable(
      name = "branch",
      doc = "The name of the target branch.\n" + "The refs/heads/ prefix is omitted.",
      structField = true,
      allowReturnNones = true)
  public String getBranch() {
    return branch;
  }

  @SkylarkCallable(
      name = "topic",
      doc = "The topic to which this change belongs.",
      structField = true,
      allowReturnNones = true)
  public String getTopic() {
    return topic;
  }

  @SkylarkCallable(
      name = "change_id",
      doc = "The Change-Id of the change.",
      structField = true,
      allowReturnNones = true)
  public String getChangeId() {
    return changeId;
  }

  @SkylarkCallable(
      name = "subject",
      doc = "The subject of the change (header line of the commit message).",
      structField = true,
      allowReturnNones = true)
  public String getSubject() {
    return subject;
  }

  public ChangeStatus getStatus() {
    return ChangeStatus.valueOf(status);
  }

  @SkylarkCallable(
      name = "status",
      doc = "The status of the change (NEW, MERGED, ABANDONED).",
      structField = true,
      allowReturnNones = true)
  public String getStatusAsString() {
    return status;
  }

  public ZonedDateTime getCreated() {
    return parseTimestamp(created);
  }

  @SkylarkCallable(
      name = "created",
      doc = "The timestamp of when the change was created.",
      structField = true,
      allowReturnNones = true)
  public String getCreatedForSkylark() {
    return created;
  }

  public ZonedDateTime getUpdated() {
    return parseTimestamp(updated);
  }

  @SkylarkCallable(
      name = "updated",
      doc = "The timestamp of when the change was last updated.",
      structField = true,
      allowReturnNones = true)
  public String getUpdatedForSkylark() {
    return updated;
  }

  public ZonedDateTime getSubmitted() {
    return parseTimestamp(submitted);
  }

  @SkylarkCallable(
      name = "submitted",
      doc = "The timestamp of when the change was submitted.",
      structField = true,
      allowReturnNones = true)
  public String getSubmittedForSkylark() {
    return submitted;
  }

  public long getNumber() {
    return number;
  }

  @SkylarkCallable(
      name = "number",
      doc = "The legacy numeric ID of the change.",
      structField = true,
      allowReturnNones = true)
  public String getNumberAsString() {
    return Long.toString(number);
  }

  @SkylarkCallable(
      name = "owner",
      doc = "The owner of the change as an AccountInfo entity.",
      structField = true,
      allowReturnNones = true)
  public AccountInfo getOwner() {
    return owner;
  }

  public ImmutableMap<String, LabelInfo> getLabels() {
    return labels == null ? ImmutableMap.of() : ImmutableMap.copyOf(labels);
  }

  @SkylarkCallable(
      name = "labels",
      doc =
          "The labels of the change as a map that maps the label names to LabelInfo entries.\n"
              + "Only set if labels or detailed labels are requested.",
      structField = true)
  public SkylarkDict<String, LabelInfo> getLabelsForSkylark() {
    return SkylarkDict.copyOf(/*environment*/ null, getLabels());
  }

  public List<ChangeMessageInfo> getMessages() {
    return messages == null ? ImmutableList.of() : ImmutableList.copyOf(messages);
  }

  @SkylarkCallable(
      name = "messages",
      doc =
          "Messages associated with the change as a list of ChangeMessageInfo entities.\n"
              + "Only set if messages are requested.",
      structField = true)
  public SkylarkList<ChangeMessageInfo> getMessagesForSkylark() {
    return SkylarkList.createImmutable(getMessages());
  }

  @SkylarkCallable(
      name = "current_revision",
      doc =
          "The commit ID of the current patch set of this change.\n"
              + "Only set if the current revision is requested or if all revisions are requested.",
      structField = true,
      allowReturnNones = true)
  public String getCurrentRevision() {
    return currentRevision;
  }

  public ImmutableMap<String, RevisionInfo> getAllRevisions() {
    return allRevisions == null ? ImmutableMap.of() : ImmutableMap.copyOf(allRevisions);
  }

  @SkylarkCallable(
      name = "revisions",
      doc =
          "All patch sets of this change as a map that maps the commit ID of the patch set to a "
              + "RevisionInfo entity.\n"
              + "Only set if the current revision is requested (in which case it will only contain "
              + "a key for the current revision) or if all revisions are requested.",
      structField = true)
  public SkylarkDict<String, RevisionInfo> getAllRevisionsForSkylark() {
    return SkylarkDict.copyOf(/*environment*/ null, getAllRevisions());
  }

  public ImmutableMap<String, List<AccountInfo>> getReviewers() {
    return reviewers == null? ImmutableMap.of(): ImmutableMap.copyOf(reviewers);
  }

  public boolean isMoreChanges() {
    return moreChanges;
  }

  @Override
  public void repr(SkylarkPrinter printer) {
    printer.append(toString());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("project", project)
        .add("branch", branch)
        .add("topic", topic)
        .add("changeId", changeId)
        .add("subject", subject)
        .add("status", status)
        .add("created", created)
        .add("updated", updated)
        .add("submitted", submitted)
        .add("number", number)
        .add("owner", owner)
        .add("labels", labels)
        .add("messages", messages)
        .add("currentRevision", currentRevision)
        .add("allRevisions", allRevisions)
        .add("moreChanges", moreChanges)
        .toString();
  }
}
