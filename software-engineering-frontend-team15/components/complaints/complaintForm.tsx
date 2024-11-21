import React, { useState } from "react";
import { useTranslation } from "next-i18next";
import ComplaintService from "@/services/ComplaintService";
import router from "next/router";
import useSWR, { mutate } from "swr";

const ComplaintForm: React.FC = () => {
  const { t } = useTranslation();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [errors, setErrors] = useState<{ title: string; description: string }>({
    title: "",
    description: "",
  });
  const [successMessage, setSuccessMessage] = useState("");

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = event.target;
    if (name === "title") {
      setTitle(value);
    } else if (name === "description") {
      setDescription(value);
    }
  };

  const validate = (): boolean => {
    let result = true;
    let newErrors = { title: "", description: "" };

    if (!title.trim()) {
      newErrors.title = "Title is required.";
      result = false;
    }
    if (!description.trim()) {
      newErrors.description = "Description is required.";
      result = false;
    }

    setErrors(newErrors);
    return result;
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validate()) {
      return;
    }
    try {
      const userEmail = sessionStorage.getItem("email");
      if (!userEmail) {
        console.error("User email not found in sessionStorage");
        return;
      }
      const response = await ComplaintService.submitComplaint({
        title,
        description,
        userEmail,
        id: 0,
      });
      if (response.ok) {
        // Display success message
        setSuccessMessage("Complaint is successfully submitted.");
        // Clear form fields
        setTitle("");
        setDescription("");
        mutate("chatOverview");
      } else {
        const errorData = await response.json();
        console.error("Error submitting complaint:", errorData);
      }
    } catch (error) {
      console.error("Error submitting complaint:", error);
    }
  };

  return (
    <div className="bg-gray-200">
      <div className="flex justify-center py-20 px-8">
        <form
          className="w-full max-w-md bg-white p-5 rounded-xl"
          onSubmit={handleSubmit}
        >
          <h1 className="text-2xl mb-6"> {t("complaint.register")}</h1>
          {successMessage && (
            <p className="text-green-500 mb-4">{successMessage}</p>
          )}
          <div className="mb-6">
            <label htmlFor="title" className="block mb-2">
              {t("complaint.title")}:
            </label>
            <input
              type="text"
              id="title"
              name="title"
              value={title}
              onChange={handleChange}
              required
              className="w-full bg-gray-200 p-1 focus:outline-none rounded-md"
            />
            {errors.title && <p className="text-red-600">{errors.title}</p>}
          </div>
          <div className="mb-6">
            <label htmlFor="description" className="block mb-2">
              {t("complaint.description")}:
            </label>
            <textarea
              id="description"
              name="description"
              value={description}
              onChange={handleChange}
              required
              className="w-full bg-gray-200 p-1 focus:outline-none rounded-md"
            />
            {errors.description && (
              <p className="text-red-600">{errors.description}</p>
            )}
          </div>
          <button
            type="submit"
            className="bg-blue-400 text-white px-4 py-2 rounded-md hover:bg-blue-500 transition-colors duration-300 ease-in-out"
          >
            {t("complaint.submit")}
          </button>
        </form>
      </div>
    </div>
  );
};

export default ComplaintForm;
