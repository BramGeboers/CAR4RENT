import React from 'react'
import NotificationOverview from '@/components/notifications/NotificationOverview'
import { serverSideTranslations } from 'next-i18next/serverSideTranslations';


const index: React.FC = () => {
  return (
    <>
            <NotificationOverview/>
    </>
  )
}

export const getServerSideProps = async (context: { locale: any; }) => {
  const { locale } = context;

  return {
    props: {
      ...(await serverSideTranslations(locale ?? "en", ["common"])),
    },
  }
}

export default index