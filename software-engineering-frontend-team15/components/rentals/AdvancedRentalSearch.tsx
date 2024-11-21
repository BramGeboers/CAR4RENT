import RentalService from '@/services/RentalService';
import { useTranslation } from 'next-i18next';
import React, { forwardRef, useState } from 'react'

const AdvancedRentalSearch = forwardRef(({setTotalPages,setRentals,page ,handleOpen}:{setRentals: any,page:number,setTotalPages: React.Dispatch<React.SetStateAction<number>>, handleOpen: (isOpen: boolean) => void } , ref) => {

    const{ t} = useTranslation()
    const [startDate, setStartDate] = useState<Date | null>(null);
    const [endDate, setEndDate] = useState<Date | null>(null);
    const [city, setCity] = useState<string|null>(null);
    const [postal, setPostal] = useState<string|null>(null);
    const [street, setStreet] = useState<string|null>(null);

    const handleReset = () => {
        setStartDate(null);
        setEndDate(null);
        setCity(null);
        setPostal(null);
        setStreet(null);
    }

    const handleSubmit = async () => {
        console.log("submitting");
    
        // If all fields are null, fetch all rentals
        if (!startDate && !endDate && !city && !postal && !street) {
            const data = await RentalService.getAllRentals(page);
            if (data.status === 200) {
                const json = await data.json();
                setTotalPages(json.totalPages);
                setRentals(json.content);
            }
            return;
        }
    
        // Otherwise, search rentals with the provided criteria
        const data = await RentalService.searchRentals({ startDate, endDate, city, postal, street }, page);
        if (data.status === 200) {
            const json = await data.json();
            console.log(json);
            setTotalPages(json.totalPages);
            setRentals(json.content);
        }
    };

    const setStartDateHandler = (date: Date | null) => {
        if (date) {
            setStartDate(date);
        }else{
            setStartDate(null);
        
        }
    }
    const setEndDateHandler = (date: Date | null) => {
        if (date) {
            setEndDate(date);
        }else{
            setEndDate(null);
        }
    }
    const setCityHandler = (city: string | null) => {
        if (city) {
            setCity(city);
        }else{
            setCity(null);
        }
    }
    const setPostalHandler = (postal: string | null) => {
        if (postal) {
            setPostal(postal);
        }else{
            setPostal(null);
        }
    }
    const setStreetHandler = (street: string | null) => {
        if (street) {
            setStreet(street);
        }else{
            setStreet(null);
        }
    }

  return (
    <div className="bg-gray-200">
        <div className="flex flex-row justify-center pt-20 pb-5 px-8 mx-auto">
          <form
            onSubmit={(e)=>{
                e.preventDefault();
                handleSubmit();}}
            onReset={handleReset}
            className="grid grid-cols-5 gap-4 mx-auto min-w-[1276px] justify-between bg-white p-5 rounded-xl"
          >
            <div className="flex flex-col mb-4">
        <label className='block'>
            {t("search.startDate")}
            <input
                name="startDate"
                type="datetime-local"
                
                onChange={(e) => setStartDateHandler(e.target.value ? new Date(e.target.value) : null)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />
        </label>
        </div>
        <div className="flex flex-col mb-4">
        <label className='block'>
            {t("search.endDate")}
            <input
                name="endDate"
                type="datetime-local"
                
                onChange={(e) => setEndDateHandler(e.target.value ? new Date(e.target.value) : null)}
                className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md"
              />
        </label>
        </div>
<div className="flex flex-col mb-4">
        <label>
            {t("search.city")}
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => setCityHandler(e.target.value)} />
            </label></div>
            <div className="flex flex-col mb-4">
        <label>

            {t("search.postal")}
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => setPostalHandler(e.target.value)} />
        </label></div>
        <div className="flex flex-col mb-4">
        <label>
            {t("search.street")}
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => setStreetHandler(e.target.value)} />
        </label></div>
        
    
        <div className='flex flex-row gap-4'>
            <input className='rounded-xl font-semibold border-red-500 border-2 bg-red-500 w-[150px] h-[55px] flex items-center justify-center text-lg hover:bg-white  ease-in-out duration-200 cursor-pointer' type="reset" value="Cancel" onClick={() => handleOpen(false)} 
        />
            <input className='rounded-xl font-semibold border-blue-400 border-2 bg-blue-400 w-[150px] h-[55px] flex items-center justify-center text-lg hover:bg-white  ease-in-out duration-200 cursor-pointer' type="submit" value="Search" />
        </div>
    </form>
    </div>
    </div>
  )
})

export default AdvancedRentalSearch